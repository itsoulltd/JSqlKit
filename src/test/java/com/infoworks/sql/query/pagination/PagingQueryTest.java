package com.infoworks.sql.query.pagination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.objects.Message;
import com.infoworks.objects.MessageParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class PagingQueryTest {

    Message consume;
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = MessageParser.getJsonSerializer();
        consume = new Message();
    }

    @After
    public void tearDown() throws Exception {
        consume = null;
    }

    @Test
    public void testConversion() throws JsonProcessingException {
        PagingQuery query = new PagingQuery();
        String json = mapper.writeValueAsString(query);
        Assert.assertTrue(json != null);
    }

    @Test
    public void testConstruction() throws IOException {
        String json = "{\"offset\":0,\"size\":10,\"previousOffset\":0,\"descriptors\":[{\"order\":\"ASC\",\"keys\":[\"name\"]},{\"order\":\"DESC\",\"keys\":[\"age\",\"salary\"]}]}";
        //String json = "{\"offset\":0,\"size\":0,\"previousOffset\":0,\"descriptors\":[{\"order\":\"ASC\",\"keys\":null}]}";
        PagingQuery query = mapper.readValue(json, PagingQuery.class);
        Assert.assertTrue(query != null);
    }

    @Test
    public void testNextConstruction() throws IOException {
        String json = "{\"offset\":0,\"size\":10,\"previousOffset\":0,\"descriptors\":[{\"order\":\"ASC\",\"keys\":[\"name\"]},{\"order\":\"DESC\",\"keys\":[\"age\",\"salary\"]}]}";

        PagingQuery query = mapper.readValue(json, PagingQuery.class);
        System.out.println("unmarshal Result 1:"+ query.toString());

        PagingQuery query2 = query.next();
        System.out.println("unmarshal Result 2:"+ query2.toString());

        PagingQuery query3 = query2.next();
        System.out.println("unmarshal Result 3:"+ query3.toString());

    }
}