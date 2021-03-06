/*****************************************************************************************
 * 
 * Copyright 2019 Gregory Brown. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 ***************************************************************************************** 
 */

package com.gabstudios.manager.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gabstudios.manager.Manageable;
import com.gabstudios.manager.ManageableExistsException;
import com.gabstudios.manager.Manager;
import com.gabstudios.manager.ManagerClosedException;


/**
 * 
 * Test class for the <code>Manager</code>
 * 
 * @author Gregory Brown (sysdevone)
 */
public class ManagerImplTest
{
    
    Manager<MockManageableImpl> _manager;
    
    @Before
    public void setup()
    {
        this._manager = new ManagerImpl<MockManageableImpl>();
    }
  
    
    @Test
    public void testCreateChildWithClassKey()
    {
        Assert.assertTrue(this._manager != null);
        final String className = "com.gabstudios.manager.impl.MockManageableImpl";
        try
        {
            final Manageable child = this._manager
                    .create(MockManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof ManageableImpl);
            Assert.assertTrue(className.equals(child.getKey()));
            
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
    }
    
    @Test
    public void testCreateChildWithClassNameKey()
    {
        Assert.assertTrue(this._manager != null);
        
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        
        try
        {
            final Manageable child = this._manager.create(className);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof ManageableImpl);
            Assert.assertTrue(className.equals(child.getKey()));
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
    }
    
    @Test
    public void testCreateChildWithKeyAndClass()
    {
        Assert.assertTrue(this._manager != null);
        
        final String key = "test-mock-o";
        
        try
        {
            final Manageable child = this._manager.create(key,
                    MockManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof ManageableImpl);
            Assert.assertTrue(key.equals(child.getKey()));
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
    }
    
    @Test
    public void testCreateChildWithKeyAndClassName()
    {
        Assert.assertTrue(this._manager != null);
        
        final String key = "test-mock-o";
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        
        try
        {
            final Manageable child = this._manager.create(key, className);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof ManageableImpl);
            Assert.assertTrue(key.equals(child.getKey()));
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
    }
    
    @Test
    public void testManagerCreation()
    {
        Assert.assertTrue(this._manager != null);
    }
    
    @Test
    public void testManagerToString()
    {
        Assert.assertTrue(this._manager.toString() != null);
    }
    
    @Test
    public void testGetChildWithClassNameKey()
    {
        
        Assert.assertTrue(this._manager != null);
        
        final String className = "com.gabstudios.manager.impl.MockManageableImpl";
        
        try
        {
            this._manager.create(className);
            
            final String key = MockManageableImpl.class.getName();
            final Manageable child = this._manager.get(key);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child.getKey().equals(key));
            Assert.assertTrue(child instanceof ManageableImpl);
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testGetChildWithKey()
    {
        
        Assert.assertTrue(this._manager != null);
        
        final String key = "test-mock-o";
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        
        try
        {
            this._manager.create(key, className);
            
            final Manageable child = this._manager.get(key);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child.getKey().equals(key));
            Assert.assertTrue(child instanceof ManageableImpl);
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testManagerClose()
    {
        this._manager.close();
        
        try
        {
            final String className = "com.gabstudios.manager.impl.ManageableImpl";
            boolean isFound = this._manager.containsChild(className);
            System.out.println("isFound=" + isFound);
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            final String className = "com.gabstudios.manager.impl.ManageableImpl";
            MockManageableImpl child = this._manager.create(className,
                    className);
            System.out.println("Child=" + child);
            Assert.fail();
        }
        catch (ManageableExistsException e)
        {
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            final String className = "com.gabstudios.manager.impl.ManageableImpl";
            MockManageableImpl child = this._manager.create(className,
                    MockManageableImpl.class);
            System.out.println("Child=" + child);
            Assert.fail();
        }
        catch (ManageableExistsException e)
        {
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            MockManageableImpl child = this._manager
                    .create(MockManageableImpl.class);
            System.out.println("Child=" + child);
            Assert.fail();
        }
        catch (ManageableExistsException e)
        {
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            final String className = "com.gabstudios.manager.impl.ManageableImpl";
            this._manager.closeChild(className);
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            final String className = "com.gabstudios.manager.impl.ManageableImpl";
            this._manager.get(className);
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        try
        {
            this._manager.getChildCount();
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        // test getKeys()
        try
        {
            this._manager.getKeys();
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
        
        // test isClose().
        boolean isClosed = this._manager.isClosed();
        Assert.assertTrue(isClosed);
        
        
        // test repeated close();
        try
        {
            this._manager.close();
            Assert.fail();
        }
        catch (ManagerClosedException e)
        {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testClose2()
    {
        Assert.assertTrue(this._manager != null);
        //final String className = "com.gabstudios.manager.BaseManageable";
        try
        {
            Manageable child = this._manager
                    .create("c1", MockManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof MockManageableImpl);
            Assert.assertTrue("c1".equals(child.getKey()));
            
            child = this._manager
                    .create("c2", MockManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof MockManageableImpl);
            Assert.assertTrue("c2".equals(child.getKey()));
            
            child = this._manager
                    .create("c3", MockManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof MockManageableImpl);
            Assert.assertTrue("c3".equals(child.getKey()));
            
            int count = this._manager.getChildCount();
            Assert.assertTrue(count == 3);
            
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        catch (final Exception e)
        {
        	e.printStackTrace();
            Assert.fail(e.toString());
        }
        
        try
        {
            this._manager.close();
            Assert.assertTrue(this._manager.isClosed());
        }
        catch (final ManagerClosedException e)
        {
        	e.printStackTrace();
            Assert.fail(e.toString());
        }
        catch (final Exception e)
        {
        	e.printStackTrace();
            Assert.fail("Unexpected Error when calling manager.close()- " +  e.toString());
        }
    }
}
