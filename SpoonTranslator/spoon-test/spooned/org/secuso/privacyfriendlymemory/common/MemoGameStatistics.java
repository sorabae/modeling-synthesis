package org.secuso.privacyfriendlymemory.common;


public class MemoGameStatistics {
    private java.util.Map<java.lang.String, java.lang.Integer> nameCountMapping = new java.util.HashMap<>();

    public MemoGameStatistics(java.util.Set<java.lang.String> statisticsSet) {
        createNameCountMapping(statisticsSet);
    }

    public void incrementCount(java.util.List<java.lang.String> resourceNames) {
        for (java.lang.String resourceName : resourceNames) {
            java.lang.Integer count = nameCountMapping.get(resourceName);
            count++;
            nameCountMapping.put(resourceName, count);
        }
    }

    public java.util.Set<java.lang.String> getStatisticsSet() {
        java.util.Set<java.lang.String> statisticsSet = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> statisticsEntry : nameCountMapping.entrySet()) {
            java.lang.String nameWithCounter = ((statisticsEntry.getKey()) + "_") + (statisticsEntry.getValue());
            statisticsSet.add(nameWithCounter);
        }
        return statisticsSet;
    }

    private void createNameCountMapping(java.util.Set<java.lang.String> statisticsSet) {
        for (java.lang.String statisticsEntry : statisticsSet) {
            java.lang.String resourceName = statisticsEntry.substring(0, statisticsEntry.lastIndexOf("_"));
            java.lang.Integer count = java.lang.Integer.parseInt(statisticsEntry.substring(((statisticsEntry.lastIndexOf("_")) + 1), statisticsEntry.length()));
            nameCountMapping.put(resourceName, count);
        }
    }

    public static java.util.Set<java.lang.String> createInitStatistics(java.util.List<java.lang.String> resourceNames) {
        java.util.Set<java.lang.String> initialStatistics = new java.util.HashSet<>();
        for (java.lang.String resourceName : resourceNames) {
            java.lang.String initialStatsEntry = (resourceName + "_") + 0;
            initialStatistics.add(initialStatsEntry);
        }
        return initialStatistics;
    }

    public java.lang.Integer[] getFalseSelectedCounts() {
        return nameCountMapping.values().toArray(new java.lang.Integer[nameCountMapping.values().size()]);
    }

    public java.lang.String[] getResourceNames() {
        return nameCountMapping.keySet().toArray(new java.lang.String[nameCountMapping.keySet().size()]);
    }
}

