package com.alibaba.graphscope.gaia;

import com.alibaba.graphscope.gaia.store.GraphStoreService;
import com.alibaba.graphscope.gaia.store.SchemaNotFoundException;
import com.alibaba.maxgraph.compiler.api.schema.GraphSchema;
import com.alibaba.maxgraph.compiler.api.schema.SchemaFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaxGraphStore extends GraphStoreService {
    private static final Logger logger = LoggerFactory.getLogger(MaxGraphStore.class);
    public static final String MAXGRAPH_MODERN_PROPERTY_RESOURCE = "maxgraph.modern.properties.json";
    private CachedMaxGraphMeta cachedGraphSchemaPair;

    public MaxGraphStore(SchemaFetcher schemaFetcher) {
        super(MAXGRAPH_MODERN_PROPERTY_RESOURCE);
        this.cachedGraphSchemaPair = new CachedMaxGraphMeta(schemaFetcher);
    }

    @Override
    public long getLabelId(String label) {
        try {
            GraphSchema graphSchema = this.cachedGraphSchemaPair.getGraphSchema();
            return graphSchema.getElement(label).getLabelId();
        } catch (Exception e) {
            logger.error("label " + label + " is invalid, please check schema");
            throw new SchemaNotFoundException("label " + label + " is invalid, please check schema");
        }
    }

    @Override
    public String getLabel(long labelId) {
        GraphSchema graphSchema = this.cachedGraphSchemaPair.getGraphSchema();
        return graphSchema.getElement((int) labelId).getLabel();
    }

    @Override
    public long getGlobalId(long labelId, long propertyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPropertyId(String propertyName) {
        try {
            GraphSchema graphSchema = this.cachedGraphSchemaPair.getGraphSchema();
            return graphSchema.getPropertyId(propertyName);
        } catch (Exception e) {
            logger.error("property " + propertyName + " is invalid, please check schema");
            throw new SchemaNotFoundException("property " + propertyName + " is invalid, please check schema");
        }
    }

    @Override
    public String getPropertyName(int propertyId) {
        GraphSchema graphSchema = this.cachedGraphSchemaPair.getGraphSchema();
        return graphSchema.getPropertyName(propertyId);
    }

    @Override
    public long getSnapShotId() {
        return this.cachedGraphSchemaPair.getSnapshotId();
    }

    @Override
    public synchronized void updateSnapShotId() {
        this.cachedGraphSchemaPair.update();
    }
}
