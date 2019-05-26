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
package com.gabstudios.manager;

import java.util.Set;

public abstract interface Manager<C extends Manageable>
{

	/**
	 * Closes the manager, and removes then closes the children. Once closed, calls to methods on the child should
	 * return a <code>ManagerClosedException</code> exception. This method calls the Manager.closeChild(key) method.
	 * 
	 */
	public abstract void close();

	/**
	 * Closes the child associated with the key.
	 * 
	 * @param key
	 *            The <code>String</code> key associated with a child.
	 * 
	 * @return Returns the child that was found and closed. If the key is not associated with child then null is
	 *         returned.
	 * 
	 */
	public abstract C closeChild(String key);

	/**
	 * Returns a <code>boolean</code> value (true or false) if a key is associated with a <code>ManagerChild</code>.
	 * 
	 * @param key
	 *            A <code>String</code> instance that is a key.
	 * @return A <code>boolean</code> value (true or false).
	 * 
	 */
	public abstract boolean containsChild(String key);

	/**
	 * Creates a child associated with a key that is the classname.
	 * 
	 * @param clazz
	 *            The class type to create a child from. Uses the Classes fully qualified name as the key.
	 * 
	 * @return A <code>ManagerChild</code> instance bound to the key.
	 * 
	 * @throws ManageableExistsException
	 *             Thrown when a Manageable instance already exists with that key.
	 */
	public abstract C create(Class<C> clazz) throws ManageableExistsException;

	/**
	 * Creates a child associated with a key that is the fully qualified className.
	 * 
	 * @param className
	 *            The fully qualified classname to create a child from. The fully qualified classname is the key.
	 * 
	 * @return A <code>ManagerChild</code> instance bound to the key.
	 * 
	 * @throws ManageableExistsException
	 *             Thrown when a Manageable instance already exists with that key.
	 */
	public abstract C create(String className) throws ManageableExistsException;

	/**
	 * Creates a child using the class type and is associated with a key.
	 * 
	 * @param key
	 *            The key to bind to the new child.
	 * @param clazz
	 *            The class type to create a child from.
	 * 
	 * @return A <code>ManagerChild</code> instance bound to the key.
	 * 
	 * @throws ManageableExistsException
	 *             Thrown when a Manageable instance already exists with that key.
	 */
	public abstract C create(String key, Class<C> clazz) throws ManageableExistsException;

	/**
	 * Creates a child using the fully qualified classname and is associated with a key.
	 * 
	 * @param key
	 *            A <code>String</code> instance. The key to bind to the new child.
	 * @param className
	 *            A <code>String</code> instance of the fully qualified classname.
	 * 
	 * @return A <code>ManagerChild</code> instance bound to the key.
	 * 
	 * @throws ManageableExistsException
	 *             Thrown when a Manageable instance already exists with that key.
	 */
	public abstract C create(String key, String className) throws ManageableExistsException;

	/**
	 * Gets the child by the bounded key.
	 * 
	 * @param key
	 *            The key that is bound to the logger.
	 * 
	 * @return An <code>ManagerChild</code> child instance associated with the key. May return null.
	 *  
	 */
	public abstract C get(String key);

	/**
	 * Returns the number of children created and managed by this Manager.
	 * 
	 * @return An integer value such that 0 &lt;= x &lt;= n is the number of children.
	 * 
	 */
	public abstract int getChildCount();

	/**
	 * Returns a <code>Set</code> containing <code>String</code> keys.
	 * 
	 * @return A <code>Set</code> containing <code>String</code> keys.
	 * 
	 */
	public abstract Set<String> getKeys();

	/**
	 * Returns a boolean (true or false) if this <code>Manager</code> is closed.
	 * 
	 * @return A <code>boolean</code> value. True if the manager is closed, otherwise it is false.
	 */
	public abstract boolean isClosed();

}