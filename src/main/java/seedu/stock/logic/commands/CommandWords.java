package seedu.stock.logic.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandWords {

    public static final String ADD_COMMAND_WORD = "add";
    public static final String BOOKMARK_COMMAND_WORD = "bookmark";
    public static final String CLEAR_COMMAND_WORD = "clear";
    public static final String DELETE_COMMAND_WORD = "delete";
    public static final String STATISTICS_COMMAND_WORD = "stats";
    public static final String EXIT_COMMAND_WORD = "exit";
    public static final String FIND_COMMAND_WORD = "find";
    public static final String FIND_EXACT_COMMAND_WORD = "findexact";
    public static final String HELP_COMMAND_WORD = "help";
    public static final String LIST_COMMAND_WORD = "list";
    public static final String UPDATE_COMMAND_WORD = "update";
    public static final String UNBOOKMARK_COMMAND_WORD = "unbookmark";
    public static final String PRINT_COMMAND_WORD = "print";
    public static final String NOTE_COMMAND_WORD = "note";
    public static final String NOTE_DELETE_COMMAND_WORD = "notedelete";
    public static final String NOTE_VIEW_COMMAND_WORD = "noteview";
    public static final String SORT_COMMAND_WORD = "sort";
    public static final String TAB_COMMAND_WORD = "tab";


    /**
     * Returns all command words existing in Warenager.
     *
     * @return A list of all command words in Warenager.
     */
    public static List<String> getAllCommandWords() {
        List<String> allCommandWords = new ArrayList<>();
        allCommandWords.add(ADD_COMMAND_WORD);
        allCommandWords.add(BOOKMARK_COMMAND_WORD);
        allCommandWords.add(CLEAR_COMMAND_WORD);
        allCommandWords.add(DELETE_COMMAND_WORD);
        allCommandWords.add(STATISTICS_COMMAND_WORD);
        allCommandWords.add(EXIT_COMMAND_WORD);
        allCommandWords.add(FIND_COMMAND_WORD);
        allCommandWords.add(FIND_EXACT_COMMAND_WORD);
        allCommandWords.add(HELP_COMMAND_WORD);
        allCommandWords.add(LIST_COMMAND_WORD);
        allCommandWords.add(UNBOOKMARK_COMMAND_WORD);
        allCommandWords.add(UPDATE_COMMAND_WORD);
        allCommandWords.add(NOTE_COMMAND_WORD);
        allCommandWords.add(NOTE_DELETE_COMMAND_WORD);
        allCommandWords.add(NOTE_VIEW_COMMAND_WORD);
        allCommandWords.add(SORT_COMMAND_WORD);
        allCommandWords.add(PRINT_COMMAND_WORD);
        allCommandWords.add(TAB_COMMAND_WORD);
        return allCommandWords;
    }
}
