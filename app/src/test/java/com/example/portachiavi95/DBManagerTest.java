package com.example.portachiavi95;

import android.os.Bundle;

import org.junit.Assert;
import org.junit.Test;

import java.io.Console;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DBManagerTest {

    @Test
    public void getDeletionQuerySelectedAccounts() {

        //DBManager dbManager = new DBManager(null, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        ArrayList<Bundle> accountsToBeDeleted = new ArrayList<>();
        String returnedQuery = DBManager.getDeletionQuerySelectedAccounts(accountsToBeDeleted);

        String expectedQuery = "DELETE FROM accounts WHERE id IN ()";

        assertEquals(expectedQuery, returnedQuery);
    }

}