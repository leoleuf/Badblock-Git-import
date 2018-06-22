package fr.badblock.toenga.sync.toenga;

import java.lang.management.ManagementFactory;
import java.util.Set;

import com.sun.management.OperatingSystemMXBean;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.TimeUtils;
import lombok.Setter;

@Setter
public class ToengaNode
{

	private String		name;
	private Set<String>	clusters;
	private long		lastProofOfExistence;
	private long		comittedVirtualMemorySize;
	private long		freePhysicalMemorySize;
	private long		freeSwapSpaceSize;
	private double		processCpuLoad;
	private double		freeProcessCpuLoad;
	private long		processCpuTime;
	private double		systemCpuLoad;
	private double		freeSystemCpuLoad;
	private long		totalPhysicalMemorySize;
	private long		usedPhysicalMemorySize;
	private long		totalSwapSpaceSize;

	public ToengaNode(String name, Set<String> clusters, long keepAlive)
	{
		setName(name);
		setClusters(clusters);
		update(keepAlive);
	}

	/**
	 * Update
	 */
	void update(long keepAlive)
	{
		setLastProofOfExistence(TimeUtils.nextTimeWithSeconds(keepAlive));
		System.out.println(getLastProofOfExistence() + " / " + TimeUtils.time());
		fillMXBean();
	}
	
	/**
	 * Fill MXBean values
	 */
	void fillMXBean()
	{
		OperatingSystemMXBean MXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		setComittedVirtualMemorySize(MXBean.getCommittedVirtualMemorySize());
		setFreePhysicalMemorySize(MXBean.getFreePhysicalMemorySize());
		setFreeSwapSpaceSize(MXBean.getFreeSwapSpaceSize());
		setProcessCpuLoad(MXBean.getProcessCpuLoad());
		setFreeProcessCpuLoad(1 - getProcessCpuLoad());
		setProcessCpuTime(MXBean.getProcessCpuTime());
		setSystemCpuLoad(MXBean.getSystemCpuLoad());
		setFreeSystemCpuLoad(1 - getSystemCpuLoad());
		setTotalPhysicalMemorySize(MXBean.getTotalPhysicalMemorySize());
		setUsedPhysicalMemorySize(getTotalPhysicalMemorySize() - getFreePhysicalMemorySize());
		setTotalSwapSpaceSize(MXBean.getTotalSwapSpaceSize());
	}
	
	/**
	 * Returns the name
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns the clusters of the node
	 * @return
	 */
	public Set<String> getClusters()
	{
		return this.clusters;
	}
	
	/**
	 * Returns if the node still exists.
	 * @return
	 */
	public boolean isValid()
	{
		return TimeUtils.isValid(getLastProofOfExistence());
	}
	
	/**
	 * Returns if the existence of the node is expired
	 * @return
	 */
	public boolean isExpired()
	{
		return !this.isValid();
	}
	
	/**
	 * Returns the last proof of existence (timestamp)
	 * @return
	 */
	public long getLastProofOfExistence()
	{
		return this.lastProofOfExistence;
	}

	/**
	 * Returns the amount of virtual memory that is guaranteed to be available to the running process in bytes, or -1 if this operation is not supported.
	 * @return
	 */
	public long getCommittedVirtualMemorySize()
	{
		return this.comittedVirtualMemorySize;
	}

	/**
	 * Returns the amount of free physical memory in bytes.
	 * @return
	 */
	public long	getFreePhysicalMemorySize()
	{
		return this.freePhysicalMemorySize;
	}

	/**
	 * Returns the amount of free swap space in bytes.
	 * @return
	 */
	public long getFreeSwapSpaceSize()
	{
		return this.freeSwapSpaceSize;
	}

	/**
	 * Returns the "recent cpu usage" for the Java Virtual Machine process.
	 * @return
	 */
	public double getProcessCpuLoad()
	{
		return this.processCpuLoad;
	}

	/**
	 * Returns the free "recent cpu usage" for the Java Virtual Machine process.
	 * @return
	 */
	public double getFreeProcessCpuLoad()
	{
		return this.freeProcessCpuLoad;
	}

	/**
	 * Returns the CPU time used by the process on which the Java virtual machine is running in nanoseconds.
	 * @return
	 */
	public long getProcessCpuTime()
	{
		return this.processCpuTime;
	}

	/**
	 * Returns the "recent cpu usage" for the whole system.
	 * @return
	 */
	private double getSystemCpuLoad()
	{
		return this.systemCpuLoad;
	}
	
	/**
	 * Returns the free"recent cpu usage" for the whole system.
	 * @return
	 */
	public double getFreeSystemCpuLoad()
	{
		return this.freeSystemCpuLoad;
	}
	
	/**
	 * Returns the total amount of physical memory in bytes.
	 * @return
	 */
	public long getTotalPhysicalMemorySize()
	{
		return this.totalPhysicalMemorySize;
	}
	
	/**
	 * Returns the used amount of physical memory in bytes.
	 * @return
	 */
	public long getUsedPhysicalMemorySize()
	{
		return this.usedPhysicalMemorySize;
	}
	
	/**
	 * Returns the total amount of swap space in bytes.
	 * @return
	 */
	public long getTotalSwapSpaceSize()
	{
		return this.totalSwapSpaceSize;
	}

	/**
	 * Returns json data
	 * @return
	 */
	public String toJson()
	{
		return GsonUtils.getGson().toJson(this);
	}
	
}
