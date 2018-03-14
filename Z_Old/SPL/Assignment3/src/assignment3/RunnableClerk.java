package assignment3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class RunnableClerk implements Runnable{
	protected Management management;
	AtomicInteger requestsNotHandledYet;
	ClerkDetails details;
	protected CyclicBarrier shiftBarrier;
	private Assets assets;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final long shiftLength =  (long) 8000;
    private final long dayLength =  (long) 24000; 
    protected AtomicInteger activeClerksLeft;

	RunnableClerk(ClerkDetails _details, Management _management,
			AtomicInteger _notHandled, AtomicInteger _activeClerks, Assets _assets, CyclicBarrier _barrier) {
		details = _details;
		management = _management;
		requestsNotHandledYet = _notHandled;
		assets = _assets;
		shiftBarrier = _barrier;
		activeClerksLeft = _activeClerks;
	}

	@Override
	public void run() {
		// Keep track of how much time (in milliseconds) the clerk waited this shift.
		long waitTime = (long) 0;
		long walkTimeLeft = (long) 0;


		RentalRequest request = null;
		Asset asset = null;
		while (requestsNotHandledYet.get() > 0) {
			while (waitTime < shiftLength) {
				LOGGER.info("Clerk " + details.getName() + " started a shift.");
				// Grab a new request.
				request = getNewReqest(waitTime);
				if (request == null) {
					// Out of requests?
					LOGGER.info("No more requests available for clerks.");
					break;
				}
				
				// Find a suitable asset.
				asset = request.findAppropriateAsset(assets);
				while (asset == null || !asset.setStatusBooked()) {
					// No asset found, wait a shift and try again.
					LOGGER.info(this.details.getName() + " waiting for an asset to become available");
					try {
						shiftBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
					waitTime = (long) 0;
					asset = request.findAppropriateAsset(assets);
				}
				// Asset was found.
				// Simulate walking to the asset.
				walkTimeLeft = (long) details.location.calculateDistance(asset.getLocation()) * 1000;
				// Back and forth.
				walkTimeLeft *= 2;
				
				
				// Walk to house.
				while (walkTimeLeft > (shiftLength - waitTime)) { //while (walkTimeLeft > (shiftLength - waitTime)) {
					// Too long for one shift walk.
					// Split this to several days.
					try {
						LOGGER.info(details.getName() + " walking to house, too far for today.");
						Thread.sleep((shiftLength - waitTime));
						// Waited a this shift, reset time waited so far.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					walkTimeLeft -= (shiftLength- waitTime);
					waitTime = (long) 0;
					
					// Wait the rest of the day.
					try {
						LOGGER.info("Clerk " + details.getName() + " finished a shift.");
						Thread.sleep(16000);
						shiftBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
				
				// Walk the rest of the distance.
				try {
					Thread.sleep(walkTimeLeft);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				waitTime += walkTimeLeft;
				walkTimeLeft = 0;
				// Got to the asset and back to the office.
				LOGGER.info(details.getName() + " got to the house");
				request.setAsset(asset);
				request.incrementStatus();
			}

			// Wait the rest of the day.
			try {
				LOGGER.info("Clerk " + details.getName() + " finished a shift.");
				Thread.sleep(16000);
				shiftBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			walkTimeLeft = (long) 0;
			waitTime = (long) 0;
		}
		
		// Try to cause all the clerks to shut down at the same time.
		activeClerksLeft.decrementAndGet();
		while(activeClerksLeft.get() > 0) {
			try {
				LOGGER.info(details.getName() + " waiting for other clerks to finish.");
				Thread.sleep(24000);
				shiftBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
		
		LOGGER.info("Shutting down clerk " + details.getName());
	}

	@Override
	public String toString() {
		return "RunnableClerk [details=" + details 
				+ ", requestsNotHandledYet=" + requestsNotHandledYet + "]";
	}
	
	/**
	 * Gets a new rental request for the clerk.
	 * @return
	 *  Unhandled rental request.
	 */
	private synchronized RentalRequest getNewReqest(long timeLeft) {
		// Get a new rental request.
		RentalRequest request = null;
		LOGGER.info(details.getName() + " attempting sync");
		synchronized(requestsNotHandledYet){
				if (requestsNotHandledYet.get() > 0) {
					LOGGER.info(details.getName() + " attempting take from queue");
						request = management.takeRequestFromQueue(timeLeft);
						if (request == null) {
							return request;
						}
						requestsNotHandledYet.decrementAndGet();
						LOGGER.info(details.getName() + " taken from queue");
				}
		}
		LOGGER.info(details.getName() + " succeeded sync");
		return request;
	}
}
