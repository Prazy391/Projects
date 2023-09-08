import java.util.*;

public class Scheduler extends Thread
{
    private Vector queue0;
    private Vector queue1;
    private Vector queue2;
    private int timeSlice;
    private static final int DEFAULT_TIME_SLICE = 1000;

    // New data added to p161 
    private boolean[] tids; // Indicate which ids have been used
    private static final int DEFAULT_MAX_THREADS = 10000;

    // A new feature added to p161 
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;
    private void initTid( int maxThreads ) {
	tids = new boolean[maxThreads];
	for ( int i = 0; i < maxThreads; i++ )
	    tids[i] = false;
    }

    // A new feature added to p161 
    // Search an available thread ID and provide a new thread with this ID
    private int getNewTid( ) {
	for ( int i = 0; i < tids.length; i++ ) {
	    int tentative = ( nextId + i ) % tids.length;
	    if ( tids[tentative] == false ) {
		tids[tentative] = true;
		nextId = ( tentative + 1 ) % tids.length;
		return tentative;
	    }
	}
	return -1;
    }

    // A new feature added to p161 
    // Return the thread ID and set the corresponding tids element to be unused
    private boolean returnTid( int tid ) {
	if ( tid >= 0 && tid < tids.length && tids[tid] == true ) {
	    tids[tid] = false;
	    return true;
	}
	return false;
    }

    // A new feature added to p161 
    // Retrieve the current thread's TCB from the queue
    public TCB getMyTcb( ) {
	Thread myThread = Thread.currentThread( ); // Get my thread object
	synchronized( queue0 ) {
	    for ( int i = 0; i < queue0.size( ); i++ ) {
		TCB tcb = ( TCB )queue0.elementAt( i );
		Thread thread = tcb.getThread( );
		if ( thread == myThread ) // if this is my TCB, return it
		    return tcb;
	    }
        for ( int i = 0; i < queue1.size( ); i++ ) {
            TCB tcb = ( TCB )queue1.elementAt( i );
            Thread thread = tcb.getThread( );
            if ( thread == myThread ) // if this is my TCB, return it
                return tcb;
        }
        for ( int i = 0; i < queue2.size( ); i++ ) {
            TCB tcb = ( TCB )queue2.elementAt( i );
            Thread thread = tcb.getThread( );
            if ( thread == myThread ) // if this is my TCB, return it
                return tcb;
        }
	}
	return null;
    }

    // A new feature added to p161 
    // Return the maximal number of threads to be spawned in the system
    public int getMaxThreads( ) {
	return tids.length;
    }

    public Scheduler( ) {
	timeSlice = DEFAULT_TIME_SLICE;
	queue0 = new Vector( );
    queue1 = new Vector();
    queue2 = new Vector();
	initTid( DEFAULT_MAX_THREADS );
    }

    public Scheduler( int quantum ) {
	timeSlice = quantum;
	queue0 = new Vector( );
    queue1 = new Vector();
    queue2 = new Vector();
	initTid( DEFAULT_MAX_THREADS );
    }

    // A new feature added to p161 
    // A constructor to receive the max number of threads to be spawned
    public Scheduler( int quantum, int maxThreads ) {
	timeSlice = quantum;
	queue0 = new Vector( );
    queue1 = new Vector();
    queue2 = new Vector();
	initTid( maxThreads );
    }

    private void schedulerSleep( int timeSlice ) {
	try {
	    Thread.sleep( timeSlice );
	} catch ( InterruptedException e ) {
	}
    }

    // A modified addThread of p161 example
    public TCB addThread( Thread t ) {
	TCB parentTcb = getMyTcb( ); // get my TCB and find my TID
	int pid = ( parentTcb != null ) ? parentTcb.getTid( ) : -1;
	int tid = getNewTid( ); // get a new TID
	if ( tid == -1)
	    return null;
	TCB tcb = new TCB( t, tid, pid ); // create a new TCB
	queue0.add( tcb );
	return tcb;
    }

    // A new feature added to p161
    // Removing the TCB of a terminating thread
    public boolean deleteThread( ) {
	TCB tcb = getMyTcb( ); 
	if ( tcb!= null )
	    return tcb.setTerminated( );
	else
	    return false;
    }

    public void sleepThread( int milliseconds ) {
	try {
	    sleep( milliseconds );
	} catch ( InterruptedException e ) { }
    }
    
    // A modified run of p161
    public void run( ) {
	Thread current = null;	
	while ( true ) {
	    try {
		// get the next TCB and its thrad
		if ( queue0.size( ) == 0 )
		    continue;
        //Queue 1
		TCB currentTCB = (TCB)queue0.firstElement( );
		if ( currentTCB.getTerminated( ) == true ) {
		    queue0.remove( currentTCB );
		    returnTid( currentTCB.getTid( ) );
		    continue;
		}
		current = currentTCB.getThread( );
		if ( current != null ) {
		    if ( current.isAlive( ) )
			current.resume();
		    else {
			// Spawn must be controlled by Scheduler
			// Scheduler must start a new thread
			current.start( );
		    }
		}
		
		schedulerSleep( 500 );
		// System.out.println("* * * Context Switch * * * ");

		synchronized ( queue0 ) {
		    if ( current != null && current.isAlive( ) )
			current.suspend();
		    queue0.remove( currentTCB ); // rotate this TCB to the end
		    queue0.add( currentTCB );
		}
        if(queue0.size() != 0){
            for(int i = 0; i < queue0.size(); i++){
                queue1.add(queue0.get(i));
            }
        }
        
        // Queue 2
        if ( queue1.size( ) == 0 )
		    continue;
		currentTCB = (TCB)queue1.firstElement( );
		if ( currentTCB.getTerminated( ) == true ) {
		    queue1.remove( currentTCB );
		    returnTid( currentTCB.getTid( ) );
		    continue;
		}
		current = currentTCB.getThread( );
		if ( current != null ) {
		    if ( current.isAlive( ) )
			current.resume();
		    else {
			// Spawn must be controlled by Scheduler
			// Scheduler must start a new thread
			current.start( );
		    }
		}
		
        if(queue0.size() != 0)
		    schedulerSleep( 1000 );
        else{
            schedulerSleep(500);
            if(queue0.size() != 0){
                for(int i = 0; i < queue0.size(); i++){
                    queue1.add(queue0.get(i));
                }
            }
            schedulerSleep(500);
        }
		// System.out.println("* * * Context Switch * * * ");

		synchronized ( queue1 ) {
		    if ( current != null && current.isAlive( ) )
			current.suspend();
		    queue1.remove( currentTCB ); // rotate this TCB to the end
		    queue1.add( currentTCB );
		}

        if(queue1.size() != 0){
            for(int i = 0; i < queue1.size(); i++){
                queue2.add(queue1.get(i));
            }
        }

        //Queue3		
		currentTCB = (TCB)queue2.firstElement();
		if ( currentTCB.getTerminated( ) == true ) {
		    queue2.remove( currentTCB );
		    returnTid( currentTCB.getTid( ) );
		    continue;
		}
		current = currentTCB.getThread();
		if ( current != null ) {
		    if ( current.isAlive( ) )
			current.resume();
		    else {
			// Spawn must be controlled by Scheduler
			// Scheduler must start a new thread
			current.start( );
		    }
		}
		for(int j = 0; j < 4; j++){
			try {
				Thread.sleep( 500 );
			} catch ( InterruptedException e ) {
			}
			if(queue0.size() != 0){
				for(int i = 0 ; i < queue0.size(); i++){
					queue2.add(queue0.get(i));
				}
			}
			if( !current.isAlive() ){
				queue2.remove(queue2.firstElement());
				currentTCB = (TCB)queue2.firstElement();
				current = currentTCB.getThread();
				if ( current.isAlive( ) )
				current.resume();
				else {
				// Spawn must be controlled by Scheduler
				// Scheduler must start a new thread
				current.start( );
				}
			}
		}
		current.suspend();

        if(queue2.size() != 0){
            for(int i = 0; i < queue2.size(); i++){
                queue2.add(queue2.remove(i));                
            }
        }

	    } catch ( NullPointerException e3 ) { };
	}
    }
}