package type;

import java.util.HashMap;
import java.util.Stack;

import ast.Function;

public class Environment<K,V>
{
	private class Scope extends HashMap<K,V> {
		final public Function scopeFunction;

		public Scope(Function scopeFunction) {
			this.scopeFunction = scopeFunction;
		}
	}

    private Stack<Scope> scopes = new Stack<Scope>();

	public void beginScope(Function function)
	{
        scopes.push(new Scope(function));
	}

	public void endScope()
	{
        scopes.pop();
	}

	public Function getScopeFunction() {
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
