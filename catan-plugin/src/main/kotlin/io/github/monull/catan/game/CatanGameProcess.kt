package io.github.monull.catan.game

import io.github.monull.catan.core.Game
import io.github.monull.catan.plugin.CatanPlugin
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.World
import org.bukkit.entity.Player

class CatanGameProcess(
    val plugin: CatanPlugin,
    val world: World
) {

    lateinit var game: Game
        private set

    lateinit var scope: CoroutineScope
        private set


    fun register(players: Set<Player>) {
        game = Game()

        scope = HeartbeatScope()

        scope.launch {
            game.launch()
        }
    }
    fun unregister() {

    }
}