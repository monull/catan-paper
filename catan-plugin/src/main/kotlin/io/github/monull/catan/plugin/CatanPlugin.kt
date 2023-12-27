package io.github.monull.catan.plugin

import io.github.monull.catan.game.CatanGameProcess
import io.github.monull.catan.game.PieceColor
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CatanPlugin : JavaPlugin() {
    var process: CatanGameProcess? = null
        private set

    override fun onEnable() {
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        for (color in PieceColor.values()) {
            val name = color.textColor.toString()
            val team = scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)
            team.color(color.textColor)
            logger.info("Team ${team.name} generated")
        }

        kommand {
            register("catan") {
                then("start") {
                    then("world" to dimension(), "players" to players()) {
                        executes {
                            val world: World by it
                            val players: Collection<Player> by it

                            kotlin.runCatching {
                                startProcess(world, players.toSet())
                            }.onFailure { exception ->
                                if (exception is IllegalStateException) {
                                    feedback(Component.text(exception.message ?: "").color(NamedTextColor.RED))
                                } else {
                                    throw exception
                                }
                            }
                        }
                    }
                }
                then("stop") {
                    executes {
                        kotlin.runCatching {
                            stopProcess()
                        }.onFailure {
                            if (it is IllegalArgumentException) {
                                feedback(Component.text(it.message ?: "").color(NamedTextColor.RED))
                            } else throw it
                        }
                    }
                }
            }
        }
    }

    override fun onDisable() {
        kotlin.runCatching { stopProcess() }
    }

    fun startProcess(world: World, players: Set<Player>): CatanGameProcess {
        check(process == null) { "process already running" }

        return CatanGameProcess(this, world).apply {
            register(players)
        }.also {
            process = it
        }
    }

    fun stopProcess(): CatanGameProcess {
        val process = requireNotNull(process) { "process is not running" }
        process.unregister()
        this.process = null

        return process
    }
}