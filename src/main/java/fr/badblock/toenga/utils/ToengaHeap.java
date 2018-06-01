package fr.badblock.toenga.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ToengaHeap<T> implements Queue<T>
{
	private List<T> elements;
	private Map<T, Integer> positions;
	
	private Comparator<T> comparator;
	private Semaphore semaphore = new Semaphore(1);
	
	public ToengaHeap(Comparator<T> comparator)
	{
		this.comparator = comparator;
		this.elements = new ArrayList<>();
		this.positions = new HashMap<>();
	}
	
	@Override
	public boolean addAll(Collection<? extends T> elements)
	{
		for(T element : elements)
		{
			if(!add(element))
				return false;
		}
		
		return true;
	}

	@Override
	public void clear()
	{
		elements.clear();
	}

	@Override
	public boolean contains(Object element)
	{
		return positions.containsKey(element);
	}

	@Override
	public boolean containsAll(Collection<?> elements)
	{
		for(Object element : elements)
		{
			if(!contains(element))
				return false;
		}
		
		return true;
	}

	@Override
	public boolean isEmpty()
	{
		return elements.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		throw new UnsupportedOperationException();
	}

	private T removeAt(int pos)
	{
		try
		{
			semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		T result = elements.get(pos);

		if(elements.size() == 1)
		{
			elements.remove(0);
			return result;
		}
		
		T element = elements.remove(elements.size() - 1);

		positions.remove(result);
		
		elements.set(pos, element);
		fixDown(0);
		
		semaphore.release();
		
		return result;
	}
	
	@Override
	public boolean remove(Object element)
	{
		if(!this.positions.containsKey(element))
		{
			return false;
		}
		
		removeAt(positions.get(element));
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> elements) {
		for(Object element : elements)
		{
			if(!remove(element))
				return false;
		}
		
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> v)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int size()
	{
		return elements.size();
	}

	@Override
	public Object[] toArray()
	{
		return elements.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray(Object[] v)
	{
		return (T[]) elements.toArray(v);
	}

	@Override
	public boolean add(T element)
	{
		try
		{
			semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		
		if(positions.containsKey(element))
		{
			int position = positions.get(element);
			
			fixDown(position);
			fixUp(position);
		}
		else
		{
			if(!elements.add(element))
				return false;
			
			positions.put(element, elements.size() - 1);
			fixUp(elements.size() - 1);
		}
		
		semaphore.release();
		return true;
	}

	@Override
	public T element()
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		
		return elements.get(0);
	}

	@Override
	public boolean offer(T v)
	{
		return add(v);
	}

	@Override
	public T peek()
	{
		return isEmpty() ? null : elements.get(0);
	}

	@Override
	public T poll()
	{
		if(isEmpty())
		{
			return null;
		}
		
		return removeAt(0);
	}

	@Override
	public T remove()
	{
		if(isEmpty())
		{
			throw new NoSuchElementException();
		}
		
		return poll();
	}
	
	private void swap(int a, int b)
	{
		T va = elements.get(a);
		T vb = elements.get(b);
		
		elements.set(a, vb);
		elements.set(b, va);
		
		positions.put(va, b);
		positions.put(vb, a);
	}
	
	private void fixDown(int position)
	{
		while(position < elements.size())
		{
			int right = (position + 1) << 1;
			int left = right - 1;
			
			int min = left;
			
			if(left >= elements.size())
				break;
			else if(right < elements.size() && comparator.compare(elements.get(left), elements.get(right) ) > 0)
				min = right;
			
			if(comparator.compare(elements.get(position), elements.get(min)) > 0)
			{
				swap(min, position);
				position = min;
			}
			else break;
		}
	}
	
	private void fixUp(int position)
	{
		while(position > 0)
		{
			int parent = ((position + 1) >> 1) - 1;

			if(comparator.compare(elements.get(position), elements.get(parent)) < 0)
			{
				swap(parent, position);
				position = parent;
			}
			else break;
		}
	}
}
