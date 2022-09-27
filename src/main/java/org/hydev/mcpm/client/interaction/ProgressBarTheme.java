package org.hydev.mcpm.client.interaction;

/**
 * Theme for the progress bar
 *
 * @param ipr The character displayed for in-progress (e.g. " ")
 * @param done The character displayed for done (e.g. "#")
 * @param iprLen Displayed length of the ipr string (Some characters have length 1 but take up two characters' space)
 * @param doneLen Displayed length of the done string
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record ProgressBarTheme(
    String ipr,
    String done,
    int iprLen,
    int doneLen
)
{
    public static final ProgressBarTheme ASCII_THEME = new ProgressBarTheme("#", "-", 1, 1);
    public static final ProgressBarTheme CLASSIC_THEME = new ProgressBarTheme("█", ".", 1, 1);
    public static final ProgressBarTheme EMOJI_THEME = new ProgressBarTheme("✅", "🕑", 2, 2);
    public static final ProgressBarTheme FLOWER_THEME = new ProgressBarTheme("🌸", "🥀", 2, 2);
}
