/**
 * Parser tester.
 */
package  abcj ;

import  java.util.* ;
import  abcj.model.abc.* ;

/**
 * @author Steve Spencer-Jowett
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestParser
{
/**
	static String  Data ="X:1				% Index\n"
	+ "T:Lynn's Backyard Waltz    \t    \t   %Title\n"
	+ "H:History line 1\n"
	+ "History line 2\n"
	+ "History line 3\n"
	+"%\n"
	+"%This is a comment\n"
	+"%\n"
	+ "M:3/4\n"
	+ "L:1/8\n"
	+ "Q:1/4=160\n"
//	+ "K:Glocrian __A _B =C ^D ^^E\n"
//	+ "K:F\n"
	+ "K:G\n"

	
//	+ "|:  \"Gmaj\"{A__B''^C,}~.vu_C'''2/3-<<<\n"			// Single note all
//	+ "(3abc\n"												// tuplet
//	+ "|:  \"Gmaj\"{A__B''^C,}~.vu[b_C'''2/3-d3]<<<\n"		// multi-note all
	
	+ "|: BA | G3 B AB | GA B2 c2 | d3 g fg | d4 B2 | cd e2 dc | Bc d2 cB\n"
	+ "|[1 A3 B cB | A4 :|[2 A3 B AG | G4 ||\n"
//	+ "Q:1/4=200\n"
//	+ "K:C#locrian   ^A   ^^B   _C   __D   =E\n"
//	+ "\\Tex Command 1\n"
//	+ "\\Tex Command 2\n"
	+ "|: Bc | d2 e2 f2 | g2 ga gf | e2 ef ed | B4 B2 | cd e2 dc | Bc d2 cB\n"
	+ "|[1 A3 B cB | A4 :|[2 A3 B AG | G4 ||\n\n" ;
**/

	static String  Data = 
	  "X:1\n"
	+ "T:Ashokan Farewell\n"
	+ "C:Jay Ungar, 1983.\n"
	+ "S:The Waltz Book\n"
	+ "N:(c) 1983 by Swinging Door Music-BMI\n"
	+ "N:Jay Ungar <fiddlerjay@aol.com>\n"
	+ "Z:John Erdman <jperdman@agate.NET>\n"
	+ "M:3/4\n"
	+ "L:1/8\n"
	+ "K:D\n"
	+ "Ac \\\n"
	+ "|| d3  cBA | F4 EF | G3 FED | B,2 D3 B, | A,2 D2 F2 |\n"
	+ "A2 d2 f2 | f3 gf2 | e4 Ac |\n"
	+ "| d3 cBA | F4 EF | G3 FED | B,2 D3 B, | A,2 D2 F2 | A2\n"
	+ "d2 f2 | A2 c2 e2 | d4 FG |\n"
	+ "| A3 FD2 | d4 A2 | B3 cd2 | A F3 E2 | F3 ED2 | B,4 G,2 |\n"
	+ " A,6 | A4 FE |\n"
	+ "| D2 F2 A2 | =c6 | B3 cd2 | A2 F2 D2 | A,2 D2 F2 | A2 d2 F2\n"
	+ "| E3 DC2 | D4 |]\n" ;




public static void  main( String[] args )
{
	new TestParser() ;
}	

	
public  TestParser()
{	
	ABCParser  Parser = new ABCParser() ;
	
	try {
		System.out.println("Start parsing") ;
//		ABCParser.setDebug(true) ;
		
		long  StartTime = System.currentTimeMillis() ;
		Parser.parse(Data) ;
		long  EndTime   = System.currentTimeMillis() ;
		
		System.out.println("parsing complete") ;
		
	//  Display back the header
		
		ABCHeader  Hdr = Parser.getABCHeader() ;
		ArrayList  Arr = Hdr.getElements() ;
		
		for ( int i = 0 ; i < Arr.size() ; i++ )
			System.out.println( ">" + ( (ABCElement) Arr.get(i) ).format() + "<" ) ;
		
	//  Display back the music
	
		System.out.println("\n\n----- Formatted tree -----\n") ;
		
		ABCMusic  Mus = Parser.getABCMusic() ;
		Arr = Mus.getElements() ;
	
		for ( int i = 0 ; i < Arr.size() ; i++ )
			System.out.println( ">" + ( (ABCElement) Arr.get(i) ).format() + "<" ) ;
		
		//  Display back the parser Log
	
			System.out.println("\n\n----- Parser Log -----\n") ;

			Arr = Parser.getLog() ;
			for ( int i = 0 ; i < Arr.size() ; i++ )
				System.out.println( Arr.get(i) ) ;

			
	//  Print parsing time
	
		System.out.println( "\nParsing time = " + ( EndTime - StartTime )+ "ms" );
	}
	catch ( Exception e ) {
		System.out.println("*** Failed *** ");
		e.printStackTrace(System.out) ;
	}
}

}
