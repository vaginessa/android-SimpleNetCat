package com.github.dddpaul.netcat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;
import java.net.Socket;

@Config( emulateSdk = 18 )
@RunWith( RobolectricTestRunner.class )
public class NetCatListenTest extends NetCatTester
{
    final static String PORT = "9998";

    /**
     * Require nc binary (netcat-openbsd package for Debian/Ubuntu).
     * nc is used to CONNECT to our NetCat.
     */
    @Before
    public void setUp() throws Exception
    {
        nc.add( "nc" );
        nc.add( "-v" );
        nc.add( HOST );
        nc.add( PORT );

        // Connect to NetCat by external nc after some delay required for NetCat listener to start
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep( 500 );
                    process = new ProcessBuilder( nc ).redirectErrorStream( true ).start();
                } catch( Exception e ) {
                    e.printStackTrace();
                }
            }
        } ).start();

        ShadowLog.stream = System.out;
        netCat = new NetCat( this );
    }

    @Test
    public void test() throws InterruptedException, IOException
    {
        // Start NetCat listener
        Socket socket = listen( PORT );
        netCat.setSocket( socket );

        testNetCatOperations();
    }
}
