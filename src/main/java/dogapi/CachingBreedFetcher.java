package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        // If already cached, return directly (no new API call)
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            // Count how many times we actually call the underlying fetcher
            callsMade++;
            List<String> subBreeds = underlyingFetcher.getSubBreeds(breed);

            // Cache only successful results
            cache.put(breed, subBreeds);
            return subBreeds;

        } catch (BreedNotFoundException e) {
            // Do NOT cache failed lookups
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
