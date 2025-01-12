class Text {
    // Enum for text formatting
    enum Formatting {
        // Basic text colors
        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        MAGENTA(35),
        CYAN(36),
        WHITE(37),

        // Bright text colors
        GRAY(90),
        BRIGHT_RED(91),
        BRIGHT_GREEN(92),
        BRIGHT_YELLOW(93),
        BRIGHT_BLUE(94),
        BRIGHT_MAGENTA(95),
        BRIGHT_CYAN(96),
        BRIGHT_WHITE(97),

        // Background colors
        BACKGROUND_BLACK(40),
        BACKGROUND_RED(41),
        BACKGROUND_GREEN(42),
        BACKGROUND_YELLOW(43),
        BACKGROUND_BLUE(44),
        BACKGROUND_MAGENTA(45),
        BACKGROUND_CYAN(46),
        BACKGROUND_WHITE(47),

        // Bright background colors
        BACKGROUND_GRAY(100),
        BACKGROUND_BRIGHT_RED(101),
        BACKGROUND_BRIGHT_GREEN(102),
        BACKGROUND_BRIGHT_YELLOW(103),
        BACKGROUND_BRIGHT_BLUE(104),
        BACKGROUND_BRIGHT_MAGENTA(105),
        BACKGROUND_BRIGHT_CYAN(106),
        BACKGROUND_BRIGHT_WHITE(107),

        // Formats
        BOLD(1),
        ITALIC(3), // May not be supported in all terminals
        UNDERLINE(4),
        BLINK(5),
        REVERSE(7), // Invert colors
        STRIKETHROUGH(9), // May not be supported in all terminals

        // Resets all, duh
        RESET_ALL(0);

        public final short code;

        // Constructor to store the corresponding ANSI code for each formatting option
        Formatting(int code) {
            this.code = (short) code;
        }

        // Utility method to generate the ANSI escape sequence string for this formatting
        public String toAnsiCode() {
            return "\033[" + this.code + "m";
        }
    }
}
