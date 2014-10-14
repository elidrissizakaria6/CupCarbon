/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package simbox_simulation;

import static org.jocl.CL.CL_CONTEXT_DEVICES;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_MAX_COMPUTE_UNITS;
import static org.jocl.CL.CL_DEVICE_TYPE_CPU;
import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_PROGRAM_BUILD_LOG;
import static org.jocl.CL.CL_QUEUE_PROFILING_ENABLE;
import static org.jocl.CL.CL_SUCCESS;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContextFromType;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clGetContextInfo;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clGetProgramBuildInfo;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clSetKernelArg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import map.Layer;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import project.Project;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Massinissa Lounis
 * @author Arezki Laga
 * @version 1.0
 */
public class GpuSimulation2 extends Thread {

	public static boolean discreteEvent = true;
	public static int step = 1 ;
	private int energyMax;
	private int iterNumber;
	private int scriptSize;
	private int nbCore;

	private int[] nbSensors;
	private int[] scriptSizeBuffer;
	private int[] min;
	private int[] energy;
	private byte[] deadSensor;
	private int[] stopCondition;

	private cl_context context;
	private cl_context_properties contextProperties;
	private cl_platform_id[] platformIDs;
	private cl_device_id[] devices;

	private cl_mem[] memNextInstruction;
	private cl_mem[] memConsumption;
	private cl_mem[] memStopCondition;

	private byte[] links;
	private int[] script;
	private int[] event;
	private int[] operationType;
	private int[] scriptIndex;

	private cl_command_queue commandQueue;

	private cl_kernel kernelNextInstruction;
	private cl_kernel kernelConsumption;
	private cl_kernel kernelStopCondition;

	private cl_program programNextInstruction;
	private cl_program programConsumption;
	private cl_program programStopCondition;

	private Pointer pMin;
	private Pointer pEvent;
	private Pointer pScript;
	private Pointer pScriptIndex;
	private Pointer pOperationType;
	private Pointer pLinks;
	private Pointer pEnergy;
	private Pointer pDeadSensor;
	private Pointer pStopCondition;
	private Pointer pNbSensors;
	private Pointer pScriptSizeBuffer;

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public GpuSimulation2() {
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void init() {
		discreteEvent = SimulationInputs.discreteEvent;
		step = SimulationInputs.step;
		energyMax = SimulationInputs.energyMax;
		iterNumber = SimulationInputs.iterNumber;
		nbSensors = new int[1];
		nbSensors[0] = SimulationInputs.nbSensors;
		
		SimulationInputs.script = null;
		SimulationInputs.links = null;
		script = SimulationInputs.gpuScript;
		links = SimulationInputs.gpuLinks;

		scriptSize = SimulationInputs.scriptSize;

		if (nbSensors[0] > 8)
			nbCore = nbSensors[0] / 8;
		else
			nbCore = 4;

		min = new int[1];
		scriptSizeBuffer = new int[1];
		scriptSizeBuffer[0] = scriptSize;

		event = new int[nbSensors[0]];
		operationType = new int[nbSensors[0]];
		scriptIndex = new int[nbSensors[0]];

		pNbSensors = Pointer.to(nbSensors);
		pScriptSizeBuffer = Pointer.to(scriptSizeBuffer);

		// loadFiles() ;

		energy = new int[nbSensors[0]];

		for (int i = 0; i < nbSensors[0]; i++)
			energy[i] = energyMax;

		deadSensor = new byte[nbSensors[0]];
		stopCondition = new int[1];
		stopCondition[0] = 1;

	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void release() {
		clReleaseMemObject(memNextInstruction[0]);
		clReleaseMemObject(memNextInstruction[1]);
		clReleaseMemObject(memNextInstruction[2]);
		clReleaseMemObject(memNextInstruction[3]);
		clReleaseMemObject(memNextInstruction[4]);
		clReleaseMemObject(memNextInstruction[5]);
		clReleaseMemObject(memNextInstruction[6]);

		clReleaseMemObject(memConsumption[0]);
		clReleaseMemObject(memConsumption[1]);
		clReleaseMemObject(memConsumption[2]);
		clReleaseMemObject(memConsumption[3]);
		clReleaseMemObject(memConsumption[4]);
		clReleaseMemObject(memConsumption[5]);
		clReleaseMemObject(memConsumption[6]);

		clReleaseMemObject(memStopCondition[0]);
		clReleaseMemObject(memStopCondition[1]);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void displayEnergy() {
		// System.out.println("ENERGY" +
		// java.util.Arrays.toString(this.energy));
		for (int i = 0; i < nbSensors[0]; i++) {
			System.out.print(energy[i] + "\t");
		}
		System.out.println();
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void getMin() {
		min[0] = event[0];
		for (int i = 1; i < nbSensors[0]; i++)
			if (min[0] > event[i])
				min[0] = event[i];
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	private int getInt(cl_device_id device, int paramName) {
		return getInts(device, paramName, 1)[0];
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	private int[] getInts(cl_device_id device, int paramName, int numValues) {
		int values[] = new int[numValues];
		clGetDeviceInfo(device, paramName, Sizeof.cl_int * numValues,
				Pointer.to(values), null);
		return values;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public int getPlatform() {

		platformIDs = new cl_platform_id[1];
		clGetPlatformIDs(platformIDs.length, platformIDs, null);

		contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platformIDs[0]);

		context = clCreateContextFromType(contextProperties,
				CL_DEVICE_TYPE_GPU, null, null, null);
		if (context == null) {
			// If no context for a GPU device could be created,
			// try to create one for a CPU device.
			context = clCreateContextFromType(contextProperties,
					CL_DEVICE_TYPE_CPU, null, null, null);
			if (context == null) {
				System.out.println("Unable to create a context");
				return -1;
			}
		}

		long numBytes[] = new long[1];

		clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, null, numBytes);

		int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
		devices = new cl_device_id[numDevices];

		clGetContextInfo(context, CL_CONTEXT_DEVICES, numBytes[0],
				Pointer.to(devices), null);

		int maxComputeUnits = getInt(devices[0], CL_DEVICE_MAX_COMPUTE_UNITS);
		System.out.printf("CL_DEVICE_MAX_COMPUTE_UNITS:\t\t%d\n",
				maxComputeUnits);

		return 0;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void createCommandeQueue() {
		commandQueue = clCreateCommandQueue(context, devices[0],
				CL_QUEUE_PROFILING_ENABLE, null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public int createProgramNextInstruction() {
		String programSource = OpenCLScriptLoader
				.loadNextInstructionOCLScript();

		programNextInstruction = clCreateProgramWithSource(context, 1,
				new String[] { programSource }, null, null);

		int stat = clBuildProgram(programNextInstruction, 0, null, null, null,
				null);

		if (stat != CL_SUCCESS) {
			System.out.println("ERREUR");
			char buildLog[] = new char[16384];
			clGetProgramBuildInfo(programNextInstruction, devices[0],
					CL_PROGRAM_BUILD_LOG, 16384, Pointer.to(buildLog), null);
			return -1;
		}
		return 0;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public int createProgramConsumption() {
		String programSource = OpenCLScriptLoader.loadConsumptionOCLScript();
		programConsumption = clCreateProgramWithSource(context, 1,
				new String[] { programSource }, null, null);

		int stat = clBuildProgram(programConsumption, 0, null, null, null, null);

		if (stat != CL_SUCCESS) {
			System.out.println("ERREUR");
			char buildLog[] = new char[16384];
			clGetProgramBuildInfo(programConsumption, devices[0],
					CL_PROGRAM_BUILD_LOG, 16384, Pointer.to(buildLog), null);
			return -1;
		}
		return 0;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public int createProgramStopCondition() {
		String programSource = OpenCLScriptLoader.loadStopConditionOCLScript();
		programStopCondition = clCreateProgramWithSource(context, 1,
				new String[] { programSource }, null, null);

		int stat = clBuildProgram(programStopCondition, 0, null, null, null,
				null);

		if (stat != CL_SUCCESS) {
			System.out.println("ERREUR");
			char buildLog[] = new char[16384];
			clGetProgramBuildInfo(programStopCondition, devices[0],
					CL_PROGRAM_BUILD_LOG, 16384, Pointer.to(buildLog), null);
			return -1;
		}
		return 0;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void createKernels() {
		kernelNextInstruction = clCreateKernel(programNextInstruction,
				"sampleKernel", null);
		kernelConsumption = clCreateKernel(programConsumption, "sampleKernel",
				null);
		kernelStopCondition = clCreateKernel(programStopCondition,
				"sampleKernel", null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void createBufferNextInstruction() {
		pEvent = Pointer.to(event);
		pScript = Pointer.to(script);
		pScriptIndex = Pointer.to(scriptIndex);
		pOperationType = Pointer.to(operationType);
		pDeadSensor = Pointer.to(deadSensor);
		pEnergy = Pointer.to(energy);

		memNextInstruction = new cl_mem[7];

		memNextInstruction[0] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0], pEvent,
				null);
		memNextInstruction[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0]
				* scriptSize * 2, pScript, null);
		memNextInstruction[2] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
				pScriptIndex, null);
		memNextInstruction[3] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
				pOperationType, null);
		memNextInstruction[4] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * 1, pScriptSizeBuffer,
				null);
		memNextInstruction[5] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_char * nbSensors[0],
				pDeadSensor, null);
		memNextInstruction[6] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0], pEnergy,
				null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void setKernelNextInstructionArg() {
		clSetKernelArg(kernelNextInstruction, 0, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[0]));
		clSetKernelArg(kernelNextInstruction, 1, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[1]));
		clSetKernelArg(kernelNextInstruction, 2, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[2]));
		clSetKernelArg(kernelNextInstruction, 3, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[3]));
		clSetKernelArg(kernelNextInstruction, 4, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[4]));
		clSetKernelArg(kernelNextInstruction, 5, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[5]));
		clSetKernelArg(kernelNextInstruction, 6, Sizeof.cl_mem,
				Pointer.to(memNextInstruction[6]));
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void launchKernelNextInstruction() {
		long global_work_size_NEXT[] = new long[] { nbSensors[0] };
		long local_work_size_NEXT[] = new long[] { nbCore };
		clEnqueueNDRangeKernel(commandQueue, kernelNextInstruction, 1, null,
				global_work_size_NEXT, local_work_size_NEXT, 0, null, null);

		clEnqueueReadBuffer(commandQueue, memNextInstruction[0], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_int, pEvent, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memNextInstruction[2], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_int, pScriptIndex, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memNextInstruction[3], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_int, pOperationType, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memNextInstruction[5], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_char, pDeadSensor, 0, null, null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void createBufferConsumption(Boolean first) {
		memConsumption = new cl_mem[7];
		pLinks = Pointer.to(links);
		pEnergy = Pointer.to(energy);
		pMin = Pointer.to(min);
		pEvent = Pointer.to(event);

		memConsumption[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, pMin, null);
		memConsumption[2] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
				pOperationType, null);
		if (first) {
			memConsumption[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
					| CL_MEM_COPY_HOST_PTR, Sizeof.cl_char * nbSensors[0]
					* nbSensors[0], pLinks, null);
			memConsumption[3] = clCreateBuffer(context, CL_MEM_READ_WRITE
					| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
					pEnergy, null);
			memConsumption[4] = clCreateBuffer(context, CL_MEM_READ_WRITE
					| CL_MEM_COPY_HOST_PTR, Sizeof.cl_char * nbSensors[0],
					pDeadSensor, null);
			memConsumption[5] = clCreateBuffer(context, CL_MEM_READ_WRITE
					| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
					pNbSensors, null);
			memConsumption[6] = clCreateBuffer(context, CL_MEM_READ_WRITE
					| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nbSensors[0],
					pEvent, null);
		}
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void setKernelConsumptionArg(Boolean first) {
		clSetKernelArg(kernelConsumption, 1, Sizeof.cl_mem,
				Pointer.to(memConsumption[1]));
		clSetKernelArg(kernelConsumption, 2, Sizeof.cl_mem,
				Pointer.to(memConsumption[2]));
		if (first) {
			clSetKernelArg(kernelConsumption, 0, Sizeof.cl_mem,
					Pointer.to(memConsumption[0]));
			clSetKernelArg(kernelConsumption, 3, Sizeof.cl_mem,
					Pointer.to(memConsumption[3]));
			clSetKernelArg(kernelConsumption, 4, Sizeof.cl_mem,
					Pointer.to(memConsumption[4]));
			clSetKernelArg(kernelConsumption, 5, Sizeof.cl_mem,
					Pointer.to(memConsumption[5]));
			clSetKernelArg(kernelConsumption, 6, Sizeof.cl_mem,
					Pointer.to(memConsumption[6]));
		}
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void launchKernelConsumption() {
		long global_work_size_2[] = new long[] { nbSensors[0] };
		long local_work_size_2[] = new long[] { nbCore };
		clEnqueueNDRangeKernel(commandQueue, kernelConsumption, 1, null,
				global_work_size_2, local_work_size_2, 0, null, null);

		clEnqueueReadBuffer(commandQueue, memConsumption[3], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_int, pEnergy, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memConsumption[6], CL_TRUE, 0,
				nbSensors[0] * Sizeof.cl_int, pEvent, 0, null, null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void createBufferFinal() {
		memStopCondition = new cl_mem[2];
		pStopCondition = Pointer.to(stopCondition);
		memStopCondition[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_char * nbSensors[0],
				pDeadSensor, null);
		memStopCondition[1] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, pStopCondition, null);
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void setKernelStopConditionArg() {
		clSetKernelArg(kernelStopCondition, 0, Sizeof.cl_mem,
				Pointer.to(memStopCondition[0]));
		clSetKernelArg(kernelStopCondition, 1, Sizeof.cl_mem,
				Pointer.to(memStopCondition[1]));
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void launchKernelFinal() {
		long global_work_size_2[] = new long[] { nbSensors[0] };
		long local_work_size_2[] = new long[] { nbCore };
		clEnqueueNDRangeKernel(commandQueue, kernelStopCondition, 1, null,
				global_work_size_2, local_work_size_2, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memStopCondition[1], CL_TRUE, 0,
				Sizeof.cl_int, pStopCondition, 0, null, null);
	}

	// ------------------------------------------------------------
	// Run Simulation
	// ------------------------------------------------------------
	@Override
	public void run() {

		System.out.println("Initialization ...");
		getPlatform();

		createProgramNextInstruction();
		createProgramConsumption();
		createProgramStopCondition();

		createCommandeQueue();

		createKernels();

		System.out.println("End of Initialization.");

		System.out.println("Start Simulation (GPU) ...");

		long startTime = System.currentTimeMillis();
		boolean first = false;
		int iter = 0;
		long time = 0;

		WsnSimulationWindow.setState("Simulation : End of initialization.");
		WsnSimulationWindow.setState("Simulate (GPU) ...");

		try {
			PrintStream ps = new PrintStream(new FileOutputStream(
					Project.getProjectResultsPath() + "/gpu_simulation.csv"));
			ps.print(time + ";");
			for (int i = 0; i < nbSensors[0]; i++) {
				ps.print(energy[i] + ";");
			}
			ps.println();
			for (iter = 0; (iter < iterNumber) && (!stopSimulation()); iter++) {
				createBufferNextInstruction();
				setKernelNextInstructionArg();
				launchKernelNextInstruction();
				
				if(discreteEvent)
					getMin();
				else
					min[0] = step;

				time += min[0];

				if (iter == 0)
					first = true;

				createBufferConsumption(first);
				setKernelConsumptionArg(first);
				launchKernelConsumption();

				createBufferFinal();
				setKernelStopConditionArg();
				launchKernelFinal();

				release();

				ps.print(time + ";");
				for (int i = 0; i < nbSensors[0]; i++) {
					ps.print(energy[i] + ";");
				}
				ps.println();
				WsnSimulationWindow.setProgress((int)(1000*iter/iterNumber));
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println(iter);
		System.out.println("End of Simulation (GPU).");
		System.out.println(((endTime - startTime) / 1000.) + " sec");
		WsnSimulationWindow.setState("End (GPU Sim) at iter " + iter + ". Simulation Time : "
				+ ((endTime - startTime) / 1000.) + " sec.");
		WsnSimulationWindow.setProgress(0);
		int i=0;
		for(Device d : DeviceList.getNodes()) {
			d.getBattery().setCapacity(energy[i++]);
		}
		Layer.getMapViewer().repaint();
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public boolean stopSimulation() {
		for (int k = 0; k < nbSensors[0]; k++) {
			if (energy[k] > 0)
				return false;
		}
		return true;
	}

}