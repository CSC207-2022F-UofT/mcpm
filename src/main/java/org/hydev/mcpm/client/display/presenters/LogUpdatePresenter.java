package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenter.Table;
import org.hydev.mcpm.client.commands.presenters.UpdatePresenter;
import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateOutcome;
import org.hydev.mcpm.client.updater.UpdateResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Formats update information for the CLI and writes it to log.
 *
 * @param log A consumer that takes Minecraft Color coded strings to print to the user.
 */
public record LogUpdatePresenter(Consumer<String> log) implements UpdatePresenter {
    private static String userShortFromType(UpdateOutcome.State state) {
        return switch (state) {
            case MISMATCHED -> "Not Found";
            case NOT_INSTALLED -> "Not Installed";
            case NETWORK_ERROR -> "Network Error";
            case UP_TO_DATE -> "Up to Date";
            case UPDATED -> "Updated";
        };
    }

    private static String colorFromType(UpdateOutcome.State state) {
        return switch (state) {
            case MISMATCHED, NOT_INSTALLED, NETWORK_ERROR -> "&c";
            case UP_TO_DATE -> "&7";
            case UPDATED -> "&2";
        };
    }

    private static String versionStringFromOutcome(UpdateOutcome outcome) {
        var initial = Optional.ofNullable(outcome.initialVersion());
        var destination = Optional.ofNullable(outcome.destinationVersion());

        // I don't really care how these are presented, but I want to handle the case.
        // Having the user see ? in a case where we don't have a plugin version seem sane.
        var initialString = initial.orElse("?");
        var destinationString = destination.orElse("?");

        return switch (outcome.state()) {
            case MISMATCHED -> "N/A";
            case NOT_INSTALLED, NETWORK_ERROR, UP_TO_DATE -> initialString;
            case UPDATED -> initialString + " -> " + destinationString;
        };
    }

    private static List<String> flattenOutcome(Map.Entry<String, UpdateOutcome> outcome) {
        var color = colorFromType(outcome.getValue().state());

        return List.of(
            color + outcome.getKey(),
            color + userShortFromType(outcome.getValue().state()),
            color + versionStringFromOutcome(outcome.getValue())
        );
    }

    private static String tabulateOutcomes(Map<String, UpdateOutcome> outcomes) {
        var headers = List.of(":Name", ":Status", "Version:");

        var rows = outcomes.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(LogUpdatePresenter::flattenOutcome)
            .toList();

        return Table.tabulate(rows, headers);
    }

    // Unsure if this should take the input, but it allows for a nicer formatting.
    @Override
    public void present(UpdateInput input, UpdateResult result) {
        var outcomes = result.outcomes();

        // If we are updating all plugins, only show users plugins that were upgraded.
        if (input.updateAll()) {
            outcomes = outcomes.entrySet().stream()
                .filter(x -> x.getValue().state() != UpdateOutcome.State.UP_TO_DATE)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        if (outcomes.isEmpty()) {
            log.accept("&2All plugins are up to date.");
        } else {
            var updated = outcomes.values().stream()
                .filter(x -> x.state() == UpdateOutcome.State.UPDATED)
                .count();

            var failed = outcomes.values().stream()
                .filter(x -> !x.state().success())
                .count();

            if (failed > 0) {
                log.accept("&cFailed to update " + failed + " plugins (" + updated + " plugins updated).");
            } else if (updated > 0) {
                log.accept("&2Updated " + updated + " plugins successfully.");
            } else {
                log.accept("&2All plugins are up to date.");
            }

            log.accept(tabulateOutcomes(outcomes));
        }
    }
}
