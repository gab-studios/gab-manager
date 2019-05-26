/*****************************************************************************************
 *
 * Copyright 2019 Gregory Brown. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 *****************************************************************************************
 */

package com.gabstudios.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gabstudios.validate.Validate;

/**
 * <pre>
 * This class is an object manager for creating and handling of child objects.  Either extended or delegate to implement.
 *
 *
 * Before an child can be created and used, the manager needs to be
 * instantiated.   It is up to the caller to determine if the manager is a
 * singleton for implementation purposes.
 *
 * Call the create(xxx) method to create and add children.
 *
 * Once a child is created, a call to the manager's getChild(key) method will
 * return the child instance associated with the key. Only one child can be
 * associated with a specific key.
 *
 * A child will be maintained by the manager until that child is removed by
 * calling the manager's removeChild(key) method or the child's close() method.
 *
 * The manager's close() method will remove all children and all observers and prevent calls to
 * other manager methods.
 *
 * </pre>
 *
 * @author Gregory Brown (sysdevone)
 */
public class BaseManager<C extends Manageable> implements Manager<C>
{
	/**
	 * The maximum length a key can be.
	 */
	public static final int	KEY_MAX_LENGTH			= 256;

	/**
	 * The maximum length a key can be.
	 */
	public static final int	CLASS_NAME_MAX_LENGTH	= 2048;

	// P = parent
	// C = child

	/*
	 * Loads an ManagerChild using the classname to get a new instances.
	 *
	 * @param className A <code>String</code> value that is a fully qualified class name.
	 * 
	 * @param <C> A type that extends <code>ManagerChild</code>.
	 * 
	 * @return A subclass of <code>ManagerChild</code>
	 */
	@SuppressWarnings("unchecked")
	protected final static <C extends Manageable> C loadManageable(final String className)
	{
		assert (className != null) : "loadManageable() - the parameter 'className' should not be null or empty";

		C child;
		try
		{
			child = (C) Class.forName(className).getDeclaredConstructor().newInstance();
		}
		catch (final IllegalAccessException e)
		{
			throw (new ManagerException("Illegal access to class name - " + className, e));
		}
		catch (final ClassNotFoundException e)
		{
			throw (new ManagerException("Unable to locate the class name - " + className, e));
		}
		catch (final InstantiationException e)
		{
			throw (new ManagerException("Unable to instantiate the class name - " + className, e));
		}
		catch (final IllegalArgumentException e)
		{
			throw (new ManagerException("Unable to instantiate the class name due to illegal argument - " + className,
			        e));
		}
		catch (final InvocationTargetException e)
		{
			throw (new ManagerException("Unable to instantiate the class name - " + className, e));
		}
		catch (final NoSuchMethodException e)
		{
			throw (new ManagerException("Unable to instantiate the class name - " + className, e));
		}
		catch (final SecurityException e)
		{
			throw (new ManagerException("Unable to instantiate the class name due to security - " + className, e));
		}
		return (child);
	}

	/*
	 * A table of children created by this manager.
	 */
	private final Map<String, C>	_children;

	/*
	 * A flag to determine if the manager has been closed.
	 */
	private boolean					_isClosed;

	/*
	 * initializes the children table.
	 */
	public BaseManager()
	{
		this._children = new HashMap<>();
		this._isClosed = false;
	}

	/*
	 * A method that adds to the Manager child table.
	 *
	 * Other classes that extend Manager can override this method for unique behavior.
	 *
	 * @param key A <code>String</code> instance that is bound to the child and used for lookups.
	 *
	 * @param child A <code>ManagerChild</code> instance that will be added to the cache.
	 *
	 * @return Returns the child that was added to the table.
	 */
	protected C addToChildTable(final String key, final C child)
	{
		assert ((key != null) && (key.trim().length() > 0)) : "addToChildTable() - the key was null, spaces or empty.";
		assert (child != null) : "addToChildTable() - the child was null.";
		this._children.put(key, child);
		return (child);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#close()
	 */
	@Override
	public void close()
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("The Manager has been closed and may not be used."));
		}
		else
		{

			// close children.
			final Set<String> keys = this.getKeys();
			for (final String key : keys)
			{
				this.closeChild(key);
			}
			assert (this._children.size() == 0) : "The child table should be empty.";

			this._isClosed = true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#closeChild(java.lang.String)
	 */
	@Override
	public C closeChild(final String key)
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{
			Validate.defineString(key).testNotNullEmpty().testMaxLength(BaseManager.KEY_MAX_LENGTH)
			        .throwValidationExceptionOnFail().validate();

			@SuppressWarnings("unchecked")
			final C child = this._children.remove(key);
			if (child != null)
			{
				child.closeWithoutRemove();
				assert (!this._children.containsKey(child
				        .getKey())) : "The children table still contains the manager child when the manager child was closed.";
			}
			return (child);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#containsChild(java.lang.String)
	 */
	@Override
	public boolean containsChild(final String key)
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{
			Validate.defineString(key).testNotNullEmpty().testMaxLength(BaseManager.KEY_MAX_LENGTH)
			        .throwValidationExceptionOnFail().validate();
			return (this._children.containsKey(key));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#create(java.lang.Class)
	 */
	@Override
	public C create(final Class<C> clazz) throws ManageableExistsException
	{
		return (this.create(clazz.getName(), clazz.getName()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#create(java.lang.String)
	 */
	@Override
	public C create(final String className) throws ManageableExistsException
	{
		return (this.create(className, className));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#create(java.lang.String, java.lang.Class)
	 */
	@Override
	public C create(final String key, final Class<C> clazz) throws ManageableExistsException
	{
		return (this.create(key, clazz.getName()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#create(java.lang.String, java.lang.String)
	 */
	@Override
	public C create(final String key, final String className) throws ManageableExistsException
	{
		Validate.defineString(key).testNotNullEmpty().testMaxLength(BaseManager.KEY_MAX_LENGTH)
		        .throwValidationExceptionOnFail().validate();
		Validate.defineString(className).testNotNullEmpty().testMaxLength(BaseManager.CLASS_NAME_MAX_LENGTH)
		        .throwValidationExceptionOnFail().validate();

		final C child = this.loadAndStoreManageable(key, className);
		child.initialize(this, key);
		return child;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#get(java.lang.String)
	 */
	@Override
	public C get(final String key)
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{
			Validate.defineString(key).testNotNullEmpty().testMaxLength(BaseManager.KEY_MAX_LENGTH)
			        .throwValidationExceptionOnFail().validate();
			// TODO - can make max length check based on the max length of a registered key.

			@SuppressWarnings("unchecked")
			final C child = this._children.get(key);
			return (child);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#getChildCount()
	 */
	@Override
	public int getChildCount()
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{
			return (this._children.size());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#getKeys()
	 */
	@Override
	public Set<String> getKeys()
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{
			final Set<String> keys = Set.copyOf(this._children.keySet());
			assert (keys != null) : "The set that holds the keys is null when it should not be.";
			return (keys);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manager#isClosed()
	 */
	@Override
	public boolean isClosed()
	{
		return (this._isClosed);
	}

	/*
	 * Loads and Stores the ManagerChild for use.
	 *
	 * @param key A <code>String</code> instance. The key to bind to the new child.
	 *
	 * @param className A <code>String</code> instance of the fully qualified classname.
	 *
	 * @return A <code>ManagerChild</code> instance bound to the key.
	 *
	 * @throws ManagerClosedException if this method is called and the Manager is closed.
	 *
	 * @throws ManageableExistsException Thrown when a Manageable instance already exists with that key.
	 */
	protected final C loadAndStoreManageable(final String key, final String className) throws ManageableExistsException
	{
		if (this.isClosed())
		{
			throw (new ManagerClosedException("This manager is closed and unable to process calls."));
		}
		else
		{

			assert ((key != null) && (key
			        .length() != 0)) : "loadAndStoreManagerChild() - the parameter 'key' should not be null or empty";
			assert ((className != null) && (className
			        .length() != 0)) : "loadAndStoreManagerChild() - the parameter 'className' should not be null or empty";

			if (this.containsChild(key))
			{
				throw (new ManageableExistsException(
				        "A Manageable instance already exists with that key='" + key + "'"));
			}
			else
			{
				C child = BaseManager.loadManageable(className);
				child = this.addToChildTable(key, child);
				return (child);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("Manager [children=");
		builder.append(this._children);
		builder.append(", isClosed=");
		builder.append(this._isClosed);
		builder.append("]");
		return builder.toString();
	}

}
