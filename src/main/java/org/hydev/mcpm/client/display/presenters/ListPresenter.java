package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.ListResultPresenter;
import org.hydev.mcpm.client.list.ListResult;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.client.display.presenters.Table.tabulate;

public class ListPresenter implements ListResultPresenter {
    private final Consumer<String> log;

    /**
     * Instantiate Install Presenter
     *
     * @param log log string to the console
     */

    public ListPresenter(Consumer<String> log) {
        this.log = log;
    }

    @Override
    public void displayResult(ListResult listResult) {
        var list = listResult.queryResult();
        if (listResult.type() == ListResult.Type.SEARCH_INVALID_INPUT) {
            log.accept(listResult.type().reason() + "\n" + "Invalid input. Please enter one of the following: " +
                    "all, manual, automatic, outdated");
        } else if (listResult.type() == ListResult.Type.SEARCH_FAILED_TO_FETCH_INSTALLED) {
            log.accept(listResult.type().reason() + "\n" + "Unable to fetch result.");
        } else {
            var table = tabulate(
                list.stream().map(p -> List.of("&a" + p.name(), "&e" + p.getFirstAuthor(), p.version())).toList(),
                List.of(":Name", "Author", "Version:"));
            this.log.accept(listResult.type().reason() + "\n" + table);
        }
    }
}
