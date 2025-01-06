package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import javafx.concurrent.Task;

import java.util.logging.Logger;

/**
 * Represents a generic background task designed for performing operations asynchronously.
 * This class extends {@link Task} and encapsulates a unit of work to be executed in the background,
 * maintaining responsiveness of the UI during long-running operations such as database queries
 * or network communications.
 *
 * @param <T> the type of the result returned by this task
 */
public class BackgroundTask<T> extends Task<T> {

    private static final Logger logger = Logger.getLogger(BackgroundTask.class.getName());
    private final TaskHandler<T> taskHandler;

    /**
     * Creates a new {@code BackgroundTask} with the specified {@link TaskHandler}.
     *
     * @param taskHandler a functional interface that defines the task to be executed in the background
     * @throws NullPointerException if {@code taskHandler} is null
     */
    public BackgroundTask(TaskHandler<T> taskHandler) {
        if (taskHandler == null) {
            throw new NullPointerException("TaskHandler cannot be null");
        }
        this.taskHandler = taskHandler;
    }

    /**
     * Executes the background task by delegating the operation to the {@link TaskHandler}.
     * If an exception occurs during execution, it is logged and rethrown to notify JavaFX of the task failure.
     *
     * @return the result of the operation performed by the {@link TaskHandler}
     * @throws Exception if an error occurs during the execution of the task
     */
    @Override
    protected T call() throws Exception {
        try {
            return taskHandler.run();
        } catch (Exception e) {
            logger.severe("Error during background task execution: " + e.getMessage());
            throw e; // Rethrow to notify JavaFX about the task failure
        }
    }

    /**
     * A functional interface representing a unit of work to be executed by the {@code BackgroundTask}.
     *
     * @param <T> the type of the result returned by this handler
     */
    @FunctionalInterface
    public interface TaskHandler<T> {

        /**
         * Executes the operation defined by the handler.
         *
         * @return the result of the operation
         * @throws Exception if an error occurs during execution
         */
        T run() throws Exception;
    }
}
