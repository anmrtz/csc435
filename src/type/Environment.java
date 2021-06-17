package type;

import java.util.HashMap;
import java.util.Stack;

public class Environment<K,V>
{
    private Stack<HashMap<K,V>> scopes = new Stack<HashMap<K,V>>();

	public void beginScope()
	{
        scopes.push(new HashMap<K,V>());
	}

	public void endScope()
	{
        scopes.pop();
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
