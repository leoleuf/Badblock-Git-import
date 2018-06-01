package fr.badblock.toenga.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import fr.badblock.toenga.utils.ToengaHeap;
import junit.framework.TestCase;

/**
 * @author LeLanN
 */
public class TestHeap extends TestCase
{
	public void testHeapStatic() throws Exception
	{
		Queue<Integer> queue = new ToengaHeap<>(new Comparator<Integer>()
		{

			@Override
			public int compare(Integer a, Integer b)
			{
				return Integer.compare(a, b);
			}
		});
		
		queue.addAll( Arrays.asList(102, 10, 30, 82, 44) );
		
		assertEquals((Integer) 10, queue.poll());
		assertEquals((Integer) 30, queue.poll());
		assertEquals((Integer) 44, queue.poll());
		assertEquals((Integer) 82, queue.poll());
		assertEquals((Integer) 102, queue.poll());
		assertEquals(true, queue.isEmpty());
		assertEquals(null, queue.poll());
	}
	
	public void testHeapRandom() throws Exception
	{
		Queue<Integer> queue = new ToengaHeap<>(new Comparator<Integer>()
		{

			@Override
			public int compare(Integer a, Integer b)
			{
				return Integer.compare(a, b);
			}
		});

		Random rand = new Random();
		int count = 1000;
		int wait = count;

		Set<Integer> ints = new HashSet<>();
		
		for(int i = 0; i < count; i++)
		{
			int random = rand.nextInt(1_000_000_000);
			
			if(ints.contains(random))
				wait--;
			else
			{
				queue.add(random);
			}
		}
		
		int last = 0;
		
		while(!queue.isEmpty())
		{
			int next = queue.remove();
			
			assertTrue(last <= next);
			last = next;
			wait--;
		}
		
		assertEquals(0, wait);
	}
}
