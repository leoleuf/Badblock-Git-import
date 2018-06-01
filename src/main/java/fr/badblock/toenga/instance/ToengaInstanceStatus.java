package fr.badblock.toenga.instance;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ToengaInstanceStatus
{
	public List<Integer> freePlaces;
	public int totalPlaces;
	public int placesPerGroup;
	public int priority;
}
