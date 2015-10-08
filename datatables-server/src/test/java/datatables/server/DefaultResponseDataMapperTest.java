package datatables.server;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultResponseDataMapperTest {

    private static class TestData {
        private static final String NAME = "-NAME-";
        private static final String SURNAME = "-SURNAME-";
        private static final Date BIRTHDATE = new Date();
    }

    @Test
    public void testMapWithoutConfiguration() {
        DefaultResponseDataMapper<TestObject> mapper = new DefaultResponseDataMapper<TestObject>();

        List<TestObject> testObjectList = new ArrayList<TestObject>();
        testObjectList.add(new TestObject(TestData.NAME, TestData.SURNAME, TestData.BIRTHDATE));

        List<Map<String, Object>> outputData = mapper.map(testObjectList);

        Assert.assertEquals(testObjectList.size(), outputData.size());
        Map<String, Object> data = outputData.get(0);

        Assert.assertEquals(TestData.NAME, data.get("name"));
        Assert.assertEquals(TestData.SURNAME, data.get("surname"));
        Assert.assertEquals(TestData.BIRTHDATE, data.get("birthDate"));
        Assert.assertEquals(3, data.size());
    }

    @Test
    public void testMapWithConfiguration() {
        Mockery context = new Mockery();

        final ColumnValueMapper columnValueMapper = context.mock(ColumnValueMapper.class);
        final String mappedNameValue = TestData.NAME.toLowerCase();

        context.checking(new Expectations() {{
            oneOf(columnValueMapper).map(TestData.NAME);
            will(returnValue(mappedNameValue));
        }});

        DefaultResponseDataMapper<TestObject> mapper = new DefaultResponseDataMapper<TestObject>();
        mapper.registerColumnValueMapper("name", columnValueMapper);

        List<TestObject> testObjectList = new ArrayList<TestObject>();
        testObjectList.add(new TestObject(TestData.NAME, TestData.SURNAME, TestData.BIRTHDATE));

        List<Map<String, Object>> outputData = mapper.map(testObjectList);

        Assert.assertEquals(testObjectList.size(), outputData.size());
        Map<String, Object> data = outputData.get(0);

        Assert.assertSame(mappedNameValue, data.get("name"));
        Assert.assertEquals(TestData.SURNAME, data.get("surname"));
        Assert.assertEquals(TestData.BIRTHDATE, data.get("birthDate"));
        Assert.assertEquals(3, data.size());

        context.assertIsSatisfied();
    }
}
