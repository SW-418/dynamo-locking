package dev.samwells.dynamolocking.dataaccess;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Component
public class DynamoLock implements ILock{
    private final DynamoDbClient dynamoDbClient;
    private final String TABLE_NAME = "dynamo-locking-locks";
    private final String TABLE_PARTITION_KEY_NAME = "lockName";
    private final String TABLE_PARTITION_KEY_VALUE = "mobile-refresh-lock";

    public DynamoLock(final DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public boolean acquire() {
        var itemMap = new HashMap<String, AttributeValue>();
        itemMap.put(TABLE_PARTITION_KEY_NAME, AttributeValue.builder().s(TABLE_PARTITION_KEY_VALUE).build());
        itemMap.put("acquiredAt", AttributeValue.builder().s(Instant.now().toString()).build());

        var putRequest = PutItemRequest
                .builder()
                .item(itemMap)
                .conditionExpression("attribute_not_exists(#pk)")
                .expressionAttributeNames(Map.of("#pk", TABLE_PARTITION_KEY_NAME))
                .tableName(TABLE_NAME)
                .build();

        try {
            dynamoDbClient.putItem(putRequest);
            System.out.println("Lock acquired");
            return true;
        } catch (Exception ex) {
            System.out.println("Encountered exception while trying to acquire lock: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean release() {
        // TODO: Could make this a conditional request as this succeeds even if the object doesn't exist
        var deleteItemRequest = DeleteItemRequest
                .builder()
                .key(Map.of(TABLE_PARTITION_KEY_NAME, AttributeValue.builder().s(TABLE_PARTITION_KEY_VALUE).build()))
                .tableName(TABLE_NAME)
                .build();

        try {
            dynamoDbClient.deleteItem(deleteItemRequest);
            System.out.println("Lock released");
            return true;
        } catch (Exception ex) {
            System.out.println("Encountered exception while trying to release lock: " + ex.getMessage());
            return false;
        }
    }
}
