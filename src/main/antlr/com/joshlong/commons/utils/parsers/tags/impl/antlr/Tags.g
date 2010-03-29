grammar Tags;

options
{
language=Java;
output=AST;
 ASTLabelType=CommonTree;
}
@lexer::members {    

 }
@parser::members {
 private  List <String> tags = new ArrayList<String>() ;
 private Object as=null; 
  public List <String> getParsedTags() throws Throwable 
  { 
  	this.tags() ; /* get it to processs */
  	List <String> nStrings = new ArrayList<String>() ; 
  	for(String t :  tags ){
 		String nt = t == null? "":t.trim(); 
 		boolean isSep = nt.equals("")|| nt.equals(","); 				 				  
 		if(!isSep) 
 			nStrings.add( nt.trim() ) ;
        } 
        return nStrings; 
  }

}
 
@lexer::header {
package com.joshlong.commons.utils.parsers.tags.impl.antlr;
import java.util.List ; 
import java.util.ArrayList; 
}
 
@parser::header {
package com.joshlong.commons.utils.parsers.tags.impl.antlr;
import java.util.List ; 
import java.util.ArrayList; 
}
 


/* 
GOOD TEST
	:	 
cow,  dog ,  'cats and dogs' ,"moo bird" #cowss  cheese march 
 yeilds :	 
 { 'cow' , 'dog','cats and dogs' , 'moo bird' , '#cowss' , 'cheese march'} 
*/
ATOMIC_STRING :  ('a'..'z'|'A'..'Z'|'0'..'9')+ |   '"' (~'"')* '"'   |   '\'' (~'\'')* '\''    ;
QUOTE 	: ('\''|'"');
SPACE 	: ' '| ' '*','' '*;
HASH_TAG:	 '#' ('a'..'z'|'A'..'Z')+   ;	
tags	 : (x=HASH_TAG { tags.add($x.text);  }
		|x=ATOMIC_STRING { tags.add($x.text);  }|x=SPACE { tags.add($x.text);  })+  ;
