package br.com.marcosaraiva.popularmovies.AsyncTasks;

/**
 * Created by marco on 21/02/2018.
 * A generic interface to be used by external Async Task classes
 */

public interface AsyncTaskExtendedInterface {
    void onTaskCompleted(Object result, String task);
}
