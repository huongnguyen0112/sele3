package com.internet.reports;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.internet.reports.providers.ReportProviderInterface;

public class ReportLoader {
    private static final ConcurrentMap<String, ReportProviderInterface> PROVIDERS = new ConcurrentHashMap<>();

    /**
     * Loads the provider for the requested report name, reusing an already loaded provider when available.
     *
     * @param reportName the report name to resolve
     * @return the provider registered for the report
     * @throws IllegalArgumentException if the report name is blank or no unique provider is available
     */
    public ReportProviderInterface loadReportProviders(String reportName) {
        if (reportName == null || reportName.isBlank()) {
                throw new IllegalArgumentException("Report name must not be blank");
        }

        String requestedReport = reportName.trim();
        String reportKey = requestedReport.toLowerCase(java.util.Locale.ROOT);
        return PROVIDERS.computeIfAbsent(reportKey, key -> loadProvider(requestedReport, reportName));
    }

    /**
     * Discovers and instantiates the service provider matching the requested report name.
     *
     * @param requestedReport the trimmed report name used for provider matching
     * @param reportName the original report name used in error messages
     * @return the matching report provider
     * @throws IllegalArgumentException if multiple providers match or no provider matches
     */
    private ReportProviderInterface loadProvider(String requestedReport, String reportName) {
        var matches = ServiceLoader
                .load(ReportProviderInterface.class)
                .stream()
                .filter(provider -> provider.type().getSimpleName()
                        .replaceFirst("ReportProvider$", "")
                        .equalsIgnoreCase(requestedReport))
                .toList();

        if (matches.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple report providers found for report: " + reportName);
        }
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("Unsupported report: " + reportName);
        }

        return matches.get(0).get();
    }
}

    

