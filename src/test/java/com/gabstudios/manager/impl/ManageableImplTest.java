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


/**
 * 
 * Test class for the <code>Manager</code>
 * 
 * @author Gregory Brown (sysdevone)
 */
public class ManageableImplTest
{
    
    Manager<ManageableImpl> _manager;
    
    @Before
    public void setup()
    {
        this._manager = new ManagerImpl<ManageableImpl>();
    }
    
    
    @Test
    public void testGetParent()
    {
        Assert.assertTrue(this._manager != null);
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child.getParent().equals(this._manager));
 
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testToString()
    {
        Assert.assertTrue(this._manager != null);
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child.toString() != null);
 
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testHashcode()
    {
        Assert.assertTrue(this._manager != null);
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child.hashCode() != 0);
 
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testEquals()
    {
        Assert.assertTrue(this._manager != null);
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child != null);
            
            ManageableImpl manageable = new ManageableImpl();
            manageable.initialize(this._manager, className);
            Assert.assertTrue(child.equals(manageable));
 
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
    
    @Test
    public void testEquals2()
    {
        Assert.assertTrue(this._manager != null);
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child.equals(child));
 
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
        
    }
  
    
    @Test
    public void testClose()
    {
        Assert.assertTrue(this._manager != null);
        final String className = "com.gabstudios.manager.impl.ManageableImpl";
        try
        {
            final Manageable child = this._manager
                    .create(ManageableImpl.class);
            Assert.assertTrue(child != null);
            Assert.assertTrue(child instanceof ManageableImpl);
            Assert.assertTrue(className.equals(child.getKey()));
            
            child.close();
            Assert.assertTrue(this._manager.getChildCount() == 0);
            
        }
        catch (final ManageableExistsException e)
        {
            Assert.fail(e.toString());
        }
    }
    
}
