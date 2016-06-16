
package net.xt57;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public
///////////////////////////////////////////////////////////////////////
class
LLog extends Object {
///////////////////////////////////////////////////////////////////////

    String          sLogPath        = "/tmp/xt57.log";
    String          sCharSet        = "utf-8";
    PrintWriter     pwLog           = null;
    boolean         bIsOpen         = false;
    int             iMasterLevel    = 1;

	LinkedList<String>	llQueue		= new LinkedList<>();

	boolean			bCachedMode		= false;

LLog() throws Exception {
/////////////////////////

    try {
        open( sLogPath );
    }
    catch (IOException e) {
        System.out.println( "log file open error" + e );
    }

}

LLog( String sInPath ) throws Exception {
/////////////////////////////////////////

	sLogPath = sInPath;
    try {
        open( sLogPath );
    }
    catch (IOException e) {
        System.out.println( "log file open error (in constr) " + e );
    }

}

public
void
open() throws Exception {
/////////////////////////

    try {
        open( sLogPath );
    }
    catch (IOException e) {
        System.out.println( "log write error" + e );
    }

}

public
void
open( String sInPath ) throws Exception {
/////////////////////////////////////////

    if ( bIsOpen )
        close();

    try {
        bIsOpen		= false;
		sLogPath	= sInPath;
        pwLog =
            new PrintWriter
                ( new BufferedWriter
                    ( new FileWriter( sLogPath, true )
                    )
                );
        bIsOpen = true;
    }
    catch (IOException e) {
        System.out.println( "log open error" + e );
    }

}

public
void
openNewPath( String sInPath ) throws Exception {
////////////////////////////////////////////////

    try {
        close();
        open( sInPath );
    }
    catch (IOException e) {
        System.out.println( "log write error" + e );
    }
}

public
void
close() {
/////////

    if ( bIsOpen ) {
        pwLog.flush();
        pwLog.close();
    }
    bIsOpen = false;
}

public
String
getStandardPreamble() {
///////////////////////
	Calendar cCal	= Calendar.getInstance();

    SimpleDateFormat ft =
        new SimpleDateFormat ("yyyy-MM-dd, HH:mm:ss.SSS");

	String pString = ft.format( cCal.getTime()	);

    pString += " : ";

	return pString;
}


public
boolean
terminateLine() {
/////////////////
	return writeAddToRawEnd( System.lineSeparator()	);
}


public
boolean
write( String sInFormat, Object... oInArgs ) {
//////////////////////////////////////////////
    return write( String.format( sInFormat, oInArgs )	);
}

public
boolean
write( String sMsgIn ) {
////////////////////////
	writeLeaveRawEnd( sMsgIn );
	terminateLine();
	return true;
}

public
boolean
writeLeaveRawEnd( String sInFormat, Object... oInArgs ) {
/////////////////////////////////////////////////////////
    return writeLeaveRawEnd( String.format( sInFormat, oInArgs )	);
}

public
boolean
writeLeaveRawEnd( String sMsgIn ) {
///////////////////////////////////
    String pString = getStandardPreamble() + sMsgIn;

	if ( bCachedMode ) {
		llQueue.add( pString );
	}////
	else {
	    pwLog.print( pString );
		pwLog.flush();
	}////

	return true;
}

public
boolean
writeAddToRawEnd( String sInFormat, Object... oInArgs ) {
/////////////////////////////////////////////////////////
    return writeAddToRawEnd( String.format( sInFormat, oInArgs )	);
}

public
boolean
writeAddToRawEnd( String sMsgIn ) {
///////////////////////////////////
    pwLog.print( sMsgIn );
	pwLog.flush();
	return true;
}////

public
boolean
flushTheQueue() {
/////////////////
	while ( llQueue.size() > 0 ) {
		String	pString	= llQueue.poll();
		if ( pString == null ) {
			return false;
		}////
		pwLog.println(	pString	 );
		pwLog.flush();
	}////
	return true;
}

public
void
setCachedMode( boolean bModeIn ) {
//////////////////////////////////
    bCachedMode = bModeIn;
}



public
void
setLoggingLevel(int iInLevel) {
///////////////////////////////
    if (iInLevel > 0 && iInLevel < 101)
        iMasterLevel = iInLevel;
}


public
int
getLoggingLevel() {
///////////////////
    return iMasterLevel;
}



public
boolean
loggingLevelIsModerate() {
//////////////////////////
    if ( iMasterLevel > 30 )
        return true;
    else
        return false;
}


public
boolean
loggingLevelIsHigh() {
//////////////////////////
    if ( iMasterLevel > 50 )
        return true;
    else
        return false;
}


public
boolean
loggingLevelIsVeryHigh() {
//////////////////////////
    if ( iMasterLevel > 75 )
        return true;
    else
        return false;
}


public
boolean
loggingLevelIsExtreme() {
/////////////////////////
    if ( iMasterLevel > 96 )
        return true;
    else
        return false;
}





public
boolean
ifVeryHigh( String sInFormat, Object... oInArgs )  {
////////////////////////////////////////////////////
    if ( loggingLevelIsVeryHigh()  )	{
        return write( String.format( sInFormat, oInArgs )	);
		//	return write( sInFormat, oInArgs );
	}//////////////////////////////////////
    else
        return false;
}




public
boolean
ifHigh( String sInFormat, Object... oInArgs )  {
////////////////////////////////////////////////
    if ( loggingLevelIsHigh()  )	{
        return write( String.format( sInFormat, oInArgs )	);
		//	return write( sInFormat, oInArgs );
	}//////////////////////////////////////
    else
        return false;
}





public
boolean
ifModerate( String sInFormat, Object... oInArgs )  {
////////////////////////////////////////////////////
    if ( loggingLevelIsModerate()  )	{
        return write( String.format( sInFormat, oInArgs )	);
		//	return write( sInFormat, oInArgs );
	}//////////////////////////////////////
    else
        return false;
}






public
boolean
ifExtreme( String sInFormat, Object... oInArgs )  {
///////////////////////////////////////////////////
    if ( loggingLevelIsExtreme()  )	{
        return write( String.format( sInFormat, oInArgs )	);
		//	return write( sInFormat, oInArgs );
	}//////////////////////////////////////
    else
        return false;
}










public
boolean
postThreadId( String sInFormat, Object... oInArgs )  {
//////////////////////////////////////////////////////
    long tId = Thread.currentThread().getId();

    String  sMsg = String.format( sInFormat, oInArgs ) + tId;

    return write( sMsg );
}





public
boolean
postThreadIdToSysOut( String sInFormat, Object... oInArgs )  {
//////////////////////////////////////////////////////
    long tId = Thread.currentThread().getId();

    String  sMsg = String.format( sInFormat, oInArgs ) + tId;

    System.out.println( sMsg );

    return true;
}


public
boolean
sleepAndLeaveANote( int iMillisIn )  {
//////////////////////////////////////
	write( "napping (%d)", iMillisIn);
	try {
		Thread.sleep(	(long) 1000	);
	} catch (Exception e) {
		;
	}
	return true;
}


   
}
// end of class
