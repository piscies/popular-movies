package br.com.marcosaraiva.popularmovies.Utilities;

/**
 * Enum to treat the Sort By Preference, set by user.
 */

public enum MovieSortByEnum  {
    MostPopular,
    HighestRated;

    public static MovieSortByEnum fromInteger(int x) {
        switch(x) {
            case 0:
                return MostPopular;
            case 1:
                return HighestRated;
        }
        return null;
    }

    public static int toInteger(MovieSortByEnum x) {
        switch(x) {
            case MostPopular:
                return 0;
            case HighestRated:
                return 1;
        }

        return -1;
    }
}
