package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Map;

/**
 * Abstract class to represent data shared by all queries
 */
public abstract class Query {

    private MetricType metric;
    private Map<AttributeType, String> filters;

    public Query(QueryBuilder b) {
        this.metric = b.getMetric();
        this.filters = b.getFilters();
    }

    public MetricType getMetric() {
        return this.metric;
    }

    public Map<AttributeType, String> getFilters() {
        return filters;
    }
}
