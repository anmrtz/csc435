package type;

import java.util.HashMap;
import java.util.Stack;

public class Environment<K,V,F>
{
	private class Scope extends HashMap<K,V> {
		final public F scopeFunction;

		public Scope(F scopeFunction) {
			this.scopeFunction = scopeFunction;
		}
	}

    private Stack<Scope> scopes = new Stack<Scope>();

	public void beginScope(F function)
	{
        scopes.push(new Scope(function));
	}

	public void endScope()
	{
        scopes.pop();
	}

	public F getScopeFunction() {
		return scopes.peek().scopeFunction;
	}

	public boolean inCurrentScope(K key)
	{
        return scopes.peek().containsKey(key);
	}

	public void add(K key, V value)
	{
        scopes.peek().put(key, value);
	}

	public V lookup(K key)
	{
        V value = scopes.peek().get(key);
        return value;
	}
}
