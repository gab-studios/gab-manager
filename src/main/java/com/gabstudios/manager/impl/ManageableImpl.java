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

package com.gabstudios.manager.impl;

import com.gabstudios.manager.Manageable;
import com.gabstudios.manager.Manager;
import com.gabstudios.manager.ManagerClosedException;
import com.gabstudios.validate.Validate;

/**
 *
 * A base implementation to extend from when creating a child that is managed by the <code>Manager</code>.
 *
 * @author Gregory Brown (sysdevone)
 */
public class ManageableImpl implements Manageable
{
	// P = parent
	// C = child

	/**
	 * A flag to determine if the child is closed. If closed, then the child should throw exception a method is called.
	 */
	private boolean		_isClosed	= false;

	/**
	 * The key bound to this child.
	 */
	private String		_key;

	/**
	 * The parent manager.
	 */
	private Manager<?>	_parent;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manageable#close()
	 */
	@Override
	public void close()
	{
		if (this._isClosed)
		{
			throw (new ManagerClosedException("This ManagerChild has been closed and may not be used."));
		}
		else
		{
			assert (this._key != null) : "close(): the key is null.";
			assert (this._parent != null) : "close(): the parent is null.";
			if (this._parent.containsChild(this._key))
			{
				this._parent.closeChild(this._key);
			}
			this._parent = null;
			this._isClosed = true;
			// this._key = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final ManageableImpl other = (ManageableImpl) obj;
		if (this._key == null)
		{
			if (other._key != null)
			{
				return false;
			}
		}
		else if (!this._key.equals(other._key))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manageable#getKey()
	 */
	@Override
	public String getKey()
	{
		assert (this._key != null) : "getKey(): the key is null.";
		return (this._key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manageable#getParent()
	 */
	@Override
	public <P extends Manager> P getParent()
	{
		if (this._isClosed)
		{
			throw (new ManagerClosedException("This ManagerChild has been closed and may not be used."));
		}
		else
		{
			assert (this._parent != null) : "getParent(): the parent is null.";
			return (P) (this._parent);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this._key == null) ? 0 : this._key.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gabstudios.manager.Manageable#initialize(com.gabstudios.manager.BaseManager, java.lang.String)
	 */
	@Override
	public <P extends Manager> void initialize(final P parent, final String key)
	{
		Validate.defineObject(parent).testNotNull().throwValidationExceptionOnFail().validate();
		Validate.defineString(key).testNotNullEmpty().throwValidationExceptionOnFail().validate();

		this._parent = parent;
		this._key = key;
		this._isClosed = false;
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
		builder.append("BaseManagerChild [_isClosed=");
		builder.append(this._isClosed);
		builder.append(", _key=");
		builder.append(this._key);
		builder.append(", _parent=");
		builder.append(this._parent.getClass());
		builder.append("]");
		return builder.toString();
	}

}
