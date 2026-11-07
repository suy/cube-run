package cube.run.core

/**
 * Bridge between a game and its host (HUD, scoring, lifecycle).
 * Safe to call from any thread (3D games call from the GL thread).
 */
interface GameSession {
    val score: Int
    val isOver: Boolean

    /** Sets the HUD score. Ignored after gameOver(). */
    fun setScore(v: Int)

    fun addScore(d: Int = 1)

    /** Called once when a run actually begins (first input), e.g. to hide pre-run options. */
    fun runStarted() {}

    /** Accepted lane changes and road remaps, reported immediately rather than sampled per frame. */
    fun laneChanged(lane: Int, laneCount: Int) {}
    fun userLaneSwipe(from: Int, to: Int) {}

    /** A physical track coin was collected, even if its net coin reward is zero. */
    fun coinPickedUp() {}

    /** One uncollected mystery box passed out of reach. Called once per box. */
    fun mysteryBoxMissed() {}

    /** Present a jackpot already included in the run coin total; never bank it twice. */
    fun jackpotWon(amount: Int) {}
    fun nearMiss() {}
    fun powerupPickedUp() {}
    fun boxCollected() {}
    /** A fatal collision while reaching for a nearby mystery box. Saves do not count. */
    fun riskyBoxDeath() {}
    /** One airborne slam reached the ground during a live run. */
    fun groundPounded() {}
    fun coalCollected() {}
    /** Bubbles or a second wind shattered [count] solid obstacles. The jackpot's shockwave does not count. */
    fun obstaclesSmashed(count: Int) {}
    /** The Ghost cube slipped through an obstacle. */
    fun obstaclePhased() {}
    /** The run ended mid jetpack flight. */
    fun diedFlying() {}
    fun fullKitHeld() {}
    /** Every coin on a jetpack flight's line was collected (the landing's checkerboard aside). */
    fun skySwept() {}
    /** A red pill ran its whole course without a crash. */
    fun redPillSurvived() {}
    fun distanceCovered(metres: Int) {}
    fun runSeconds(seconds: Int) {}
    fun runCrashed(seconds: Float) {}

    /** Bank the cumulative run total now, without advancing a jackpot presentation on the HUD. */
    fun bankRunCoins(total: Int) {}

    /** Coins collected so far this run; banked immediately and shown on the HUD. */
    fun setCoins(v: Int) {}

    /** Bubble shields left in stock (shown as a HUD hint). */
    fun setBubbles(v: Int) {}
    fun setBubbleCooldown(seconds: Int) {}
    /** Rows score double right now (the 2× pickup or a Double Score bubble): the HUD score turns pink
     *  and shows an ×N badge. 1 (or less) means no boost. */
    fun setScoreBoost(factor: Int) {}

    /** A row scored under a stacked score multiplier: [bonus] points beyond the base one. */
    fun boostedRowScored(bonus: Int) {}

    /** Near-miss bonus points, Close Shave's triple included. */
    fun nearMissScored(points: Int) {}

    /**
     * Extra coins beyond the base coin value, attributed to their boost:
     * 0 = the cube's coin multiplier, 1 = the Golden Bubble, 2 = a coin-doubling bonus world.
     */
    fun coinBonus(source: Int, amount: Double) {}

    /** Mystery boxes collected so far this run (opened on the run-over screens). */
    fun setBoxes(v: Int) {}

    /** One collectible shard; banked immediately. */
    fun addShard(kind: Int) {}

    /** The world the player is running through (shown on the results). */
    fun setWorld(name: String) {}

    /** The fire-boost window opened/closed ([taps] used so far, [max] allowed). */
    fun setBoost(open: Boolean, taps: Int, max: Int) {}

    /** A portal was entered (a `data.Bonus` id) or left (-1). */
    fun setBonus(id: Int) {}

    /**
     * The 3D gift stage just opened a box (see [Stage]); [kind] is a
     * `Progress.BoxReward` kind; [cat]/[id] name the wardrobe item for SKIN.
     */
    fun boxOpened(kind: Int, amount: Int, cat: Int, id: Int) {}

    /**
     * Ends the run: saves the high score, plays the
     * success/fail sound + haptic, and shows the run-over screens.
     * The game's update loop keeps running afterwards (for death animation) —
     * guard gameplay logic with [isOver].
     */
    fun gameOver()
}

/** What the boosts added to a run, shown as pills on the results card. */
data class RunBreakdown(
    /** Rows that scored under a 2× / Double Score multiplier. */
    val boostedRows: Int = 0,
    /** Extra points those rows scored beyond the base one per row. */
    val boostPoints: Int = 0,
    /** Near-miss bonus points, Close Shave's triple included. */
    val nearMissPoints: Int = 0,
    /** Extra coins from the cube's coin multiplier. */
    val skinCoins: Double = 0.0,
    /** Extra coins from the Golden Bubble. */
    val bubbleCoins: Double = 0.0,
    /** Extra coins inside a coin-doubling bonus world. */
    val worldCoins: Double = 0.0,
    /** The cube worn this run, to name the coin multiplier pill. */
    val skinId: Int = 0,
)
