package core

/**
 * Represents the lifecycle state of the KissEngine.
 *
 * This is a strict finite-state model.
 */
enum class EngineState {
    /** Engine is initialized but not running */
    IDLE,

    /** Engine is actively listening and detecting */
    RUNNING,

    /** Engine was stopped by the user */
    STOPPED,

    /** Engine encountered an unrecoverable error */
    ERROR
}
