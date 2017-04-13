package MyGraph;

import java.awt.*;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import java.lang.Math;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import MyUtils.CoorSys;
import MyUtils.GraphPoints;
import MyUtils.GraphsHolder;
import MyUtils.Struct;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/* Dozwolone konstrukcje i znaki */
  // sin() cos() tan() ctg() sqr() lgn() abs()
  // x - zmienna
  // e - stała Eulera
  // p - stała PI
  // dozwolone cyfry 0-9 
  // kropka jako separator liczb zmiennoprzecinkowych
  // dozwolone operatory / * - + ();
/* koniec */

class GraphPanel extends JPanel implements MouseMotionListener, MouseListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Lista rozwijalna wykresów*/
	private JComboBox<String> t2;
	
	private int fullWidth;
    private int fullHeight;
    private int halfWidth;
    private int halfHeight;
     /* pola obsługiwane przez funkcje parsujace */
		private String pattern;
	    private int iterator;
	    private ArrayList<Struct> onpList;
	    private ArrayList<Struct> list;
	    private Stack<Struct> stack;
	/* koniec */
    private String SX;
    private String SY;
    private DecimalFormat dF;
    private CoorSys system;
    private GraphsHolder graphs;
    
    private double LowIntegralLim;
    private double HighIntegralLim;
    
    private boolean excFlag; // flaga wystapienia błędu
    
    private double COORXPOS;
    private double COORYPOS;
    
    public boolean allowGraph; 
    public boolean allowCorSys;
    public boolean allowIntegral;
    public int scaleX;
    public int scaleY;
    public double delta;
    public double Period;
    
    public PanelRight panelRight;  
    public ButtonPanel buttonPanel;
    
    
	
    
    public GraphPanel(){
    			this.setLayout(new FlowLayout(FlowLayout.LEFT));
    			t2 = new JComboBox<String>();
    			this.add(t2);
    			
				addMouseMotionListener(this);
				addMouseListener(this);
				setVisible(true);
				this.graphs = new GraphsHolder();
				this.allowGraph=false;
				this.allowCorSys=true;
				this.allowIntegral=false;
				this.HighIntegralLim = 0.00;
				this.LowIntegralLim = 0.00;
				this.excFlag = false;
				this.scaleX=50;
				this.scaleY=50;
				this.delta =0.01;
				this.COORXPOS = 0;
				this.SX = null;
				this.COORYPOS = 0;
				this.SY = null;
				this.panelRight=null;
				/* inicjalizacja pól parsera */
				this.iterator=0;
				this.pattern=null;
				this.stack = new Stack<Struct>();
				this.list=new ArrayList<Struct>();
				this.onpList = new ArrayList<Struct>();
				this.system = CoorSys.CARTESIAN;
				/* koniec */
				DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
				otherSymbols.setDecimalSeparator('.');
				this.dF = new DecimalFormat("#.####",otherSymbols);
				
			}

   @Override
    protected void paintComponent(Graphics g){
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
            this.fullWidth = this.getWidth();
            this.fullHeight = this.getHeight();
            this.halfWidth = this.fullWidth/2;
            this.halfHeight = this.fullHeight/2;
		if(allowGraph){
			initGraph(g2d);
		}
		if(this.allowCorSys){
			initCoordinateSysytem(g2d);
		}
		if(this.excFlag){
			this.allowCorSys = true;
			this.excFlag = false;
		}
	}
   
/* funkcja rysuje całkę */
   public void drawIntegral(Graphics2D g2d){
	   	 GraphPoints gp = this.getSelectedGraph();
	     double l=LowIntegralLim;
	     double h=HighIntegralLim;
	     double x,y;
	     int height;
	     int xx,yy;
	     height= this.getHeight()/2;
	     g2d.setColor(gp.getColor());
	     if(l<=h){
			 while(l<=h){
				  x=l;
				  y = getResult(l);
				  y=halfHeight-(y*scaleY);
				  x=halfWidth +(x*scaleX);
				  yy =(int) Math.round(y);
				  xx =(int) Math.round(x);
				  g2d.drawLine(xx,
						  	   height,
							   xx,
							   yy
							  );
	
				 l=l+this.delta;   
		     }
	     }else{
			 while(l>h){
				  x=l;
				  y = getResult(l);
				  y=halfHeight-(y*scaleY);
				  x=halfWidth +(x*scaleX);
				  yy =(int) Math.round(y);
				  xx =(int) Math.round(x);
				  g2d.drawLine(xx,
						  	   height,
							   xx,
							   yy
							  );
	
				 l=l-this.delta;   
		     }	 
	     }
   }
/* funkcja rysująca układ współrzędnych */
	private void initCoordinateSysytem(Graphics2D g2d){
			g2d.setColor(Color.BLACK);
			g2d.drawLine(0,(this.getHeight()/2),this.fullWidth,(this.fullHeight/2));
			g2d.drawLine((this.getWidth()/2),0,(this.fullWidth/2),this.fullHeight);
        /* podziałka */
           /*pozioma*/
           int counter = this.fullWidth/this.scaleX;
           while (counter > 0){
			    g2d.drawLine(
					halfWidth+(scaleX*counter),
					(halfHeight+10),
					(halfWidth+(scaleX*counter)),
					(halfHeight-10)
				  );
				 g2d.drawLine(
					halfWidth-(scaleX*counter),
					(halfHeight+10),
					(halfWidth-(scaleX*counter)),
					(halfHeight-10)
				  );
			    counter=counter - 1;
			   } 
		   /* pionowa */	   
           counter = fullHeight/scaleY;
           while (counter > 0){
                g2d.drawLine(
                        (halfWidth -10),
                        (halfHeight+(scaleY*counter)),
                        (halfWidth +10),
                        (halfHeight+(scaleY*counter))
                            );
                g2d.drawLine(
                        (halfWidth -10),
                        (halfHeight-(scaleY*counter)),
                        (halfWidth +10),
                        (halfHeight-(scaleY*counter))
                            );
			    counter=counter - 1;
			   }
           counter = fullHeight/scaleY;        
	}

/* ustawienie zmiennej prywatnej wzoru */
 public void insertPattern(String pattern){
	    this.pattern=new String(pattern);
}
	   

/* Funkcja sprawdzająca poprawność wprowadzonych znaków*/
private boolean checkAlphabet(String pattern){
		  char[] txt = pattern.toCharArray(); 
		  for(char c : txt){
			  switch(c){
				  /*matryca poprawnych znaków*/
				   case '1': case '2': case '3':
				   case '4': case '5': case '6': 
				   case '7': case '8': case '9':
				   case '0': case '^': case '/':
				   case '(': case ')': case '*':
				   case '+': case '-': case '.': 
				   case 'x': case 'e': case 'p':
				   case 's': case 'i': case 'n':
				   case 'c': case 'o': case 't':
				   case 'g': case 'a': case 'q':
				   case 'r': case 'l': case 'b':
					   
				   
				   break;
				   default: 
				    return false;
				  }
			  }
		  return true;
}

/* Funkcja sprawdzająca poprawność konstrukcji całości wzoru */
private boolean parsePattern(){
	 if (!checkAlphabet(this.pattern)){
		  return false;
		 }
     return true;
	}

/* funkcje parsujące */


private double getDouble(String pattern){
			char[] str = pattern.toCharArray();
			String doub = "";
			double result = 0.0;
			for(int i = this.iterator ;i<pattern.length();i++){
				 switch(str[i]){
					  case '0': case '1': case '2': case '3': case '4':
					  case '5': case '6': case '7': case '8': case '9':
					  case '.':
					   doub= doub + str[i];
					  break;	  
					  default :
					   result = Double.parseDouble(doub);
					   return result;
					 }
				this.iterator = i;
				}

			result = Double.parseDouble(doub);
			return result;
}

/* sprawdza czy podany znak jest operatorem */
private boolean isOper(char x){
	 switch(x){
		  case '+': case '-': case '*': case '/': case '^': case '(': case ')':
		   return true;
		 }
	  return false;
	}
	
/* sprawdza czy podany znak jest funkcją*/
private boolean isFOper(char x){
	 switch(x){
		  case 's': case 'c': case 't': case 'q': case 'r': case  'l': case 'a':
		   return true;
		 }
	  return false;
	}	
	
/* sprawdza czy podany znak jest cyfrą */
private boolean isNum(char x){
	 switch(x){
		  case '0': case '1': case '2': case '3': case '4': 
		  case '5': case '6': case '7': case '8': case '9':
		   return true;
		 }
	  return false;
	}
	
	
/* sprawdza czy podany znak jest zmienną */
private boolean isVar(char x){
  switch(x){
		  case 'x':
		   return true;
		 }
	  return false;
	}
	
	

/* sprawdza czy znak jest funkcją */
private boolean isConst(char x){
	switch(x){
		  case 'p': case 'e': 
		   return true;
		 }
	  return false;
	}
	
	
/* wydaje stałą na podstawie znaku */
private double getConst(char x){
	switch(x){
		  case 'p':
		   return Math.PI;
		  case 'e':
		   return Math.E;
		 }
	  return 0.00;
}


/* zwraca priorytet operatora */
private int getPriority(char c){
	switch(c){
		case '(' :
			return 0;
		case '+': case '-' :
		    return 1;
		case '*': case '/':
            return 2;
        case '^': 
            return 3;
        case 's': case 'c': case 't': case 'q': case  'r' : case 'l': case 'a':
			return 4;
      	}
    return 0;
}

/* zwraca znak funkcji operatora korzysta z głownego iteratora i wzoru*/
private char getFunc(){
	 char[] arr =  this.pattern.toCharArray();
	 int i = this.iterator;
	 this.iterator =this.iterator + 2;
	 switch(arr[i]){
		 case 's' : 	  
			  if(arr[i+1]=='i'){
				return 's';  
			  }else{ 
			   return 'r';
			  }
		 case 'c':
			  if(arr[i+1]=='o'){
				return 'c';  
			  }else{ 
			   return 'q';
			  }
		 case 't':
		  return 't';
		 case 'l':
		  return 'l'; // logarytm naturalny
		 case 'a':
		  return 'a';
			 
	  }
	 return '#';
	}


/* zwraca wynik działania */

private double getResult(double x){
	 Struct st,v1,v2;
	 int i,size;
	 size = this.onpList.size();
	 for(i=0;i<size;i++){
		  st = this.onpList.get(i);
		  switch(st.s){
			   case '#': this.stack.push(new Struct('x',st.n)) ; break;
			   case 'x': 
					st.n=x;
					this.stack.push(new Struct('x',st.n)) ;
				break;
			  case '+': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v1.n + v2.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '-': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v2.n - v1.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '*': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v1.n * v2.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '/': 
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = v2.n / v1.n;
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case '^': //potęga
				v1 = this.stack.pop();
				v2 = this.stack.pop();
				v1.n = Math.pow(v2.n ,v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 's': //sinus
				v1 = this.stack.pop();
				v1.n = Math.sin(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'c': // cosinus
				v1 = this.stack.pop();
				v1.n = Math.cos(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 't': // tangens
				v1 = this.stack.pop();
				v1.n = Math.tan(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'q': //cotangens
				v1 = this.stack.pop();
				v1.n =1.00/Math.tan(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'r': //pierwiastek
				v1 = this.stack.pop();
				v1.n =Math.sqrt(v1.n);
				this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'l': //logarytm naturalny
					v1 = this.stack.pop();
					v1.n =Math.log(v1.n);
					this.stack.push(new Struct('#',v1.n));
			   break;
			  case 'a': //wartosc absolutna
					v1 = this.stack.pop();
					v1.n =Math.abs(v1.n);
					this.stack.push(new Struct('#',v1.n));
			   break;
			  case '=': 
				v1 = this.stack.pop();
				return v1.n;
			  }

		 }
	return 0.00;
}
 
/* ładuje łańcuch znaków do listy wzoru operuje na polach prywatnych iterator , list oraz uzywa pola pattern */	
private void toList(){
		   this.list.clear();
		   this.iterator = 0;
		   char[] str = this.pattern.toCharArray();
		   int len = str.length;
		   
		  while(this.iterator<len){
			
			if(isNum(str[this.iterator])){									//jeżeli cyfra
				
			   this.list.add(new Struct('#',getDouble(this.pattern)));
			   if((len>1) && (this.iterator<len-1)){
				  if(isVar(str[this.iterator+1])){
					 this.list.add(new Struct('*',0.00));
				  }
			   }
			   	   
			}else if(isOper(str[this.iterator])){							//jezeli operator
				if((this.iterator == 0) && (str[this.iterator] == '-')){
					if((isNum(str[this.iterator+1])) || (isVar(str[this.iterator+1])) || (str[this.iterator+1] == '(') || (isFOper(str[this.iterator+1]))){
						this.list.add(new Struct('#',-1.00));
						this.list.add(new Struct('*',0.00));
					}
				}else if(str[this.iterator] == '('){
				  this.list.add(new Struct(str[this.iterator],0.00));
				  if(str[this.iterator+1]=='-'){
							this.iterator = this.iterator + 1;
							this.list.add(new Struct('#',-1.00));
							this.list.add(new Struct('*',0.00));
					}
				}else{
					this.list.add(new Struct(str[this.iterator],0.00));
				}

			}else if(isVar(str[this.iterator])){							//jezeli zmienna
				
				this.list.add(new Struct(str[this.iterator],0.00));					
				
			}else if(isFOper(str[this.iterator])){							//jeżeli funkcja
				
				this.list.add(new Struct(this.getFunc(),0.00));		
												
			}else if(isConst(str[this.iterator])){							// jeżeli stała
				
				this.list.add(new Struct('#',this.getConst(str[this.iterator])));
				
			}
			this.iterator = this.iterator +1;
	      }
		  this.list.add(new Struct('=',0.00));
}
  
    
/* przekształca listę z wyrażeniem w zwykłej postaci na listę w postaci onp */
private void toOnpList(){
		this.onpList.clear();
	   /* kod przekształcający */
	   int i;
	   Struct st;
	   for(i=0;i<this.list.size();i++){
		    st = this.list.get(i);
		    if(st.s=='='){
				while(!this.stack.empty()){
					 this.onpList.add(this.stack.pop());
					}
			  break;
			}else{
			   if((st.s =='#') || (isVar(st.s)) ){
				 this.onpList.add(st);  
			   }else if(st.s =='('){
				 this.stack.push(st); 
			   }else if(st.s ==')'){
				   while(this.stack.peek().s!='('){
					    this.onpList.add(this.stack.pop());
					   }
				   this.stack.pop();
		       }else if(isOper(st.s) || isFOper(st.s)){
				  while((!this.stack.empty()) && ((getPriority(st.s) < getPriority(this.stack.peek().s))) ){
					 this.onpList.add(this.stack.pop()); 
				  } 
				  this.stack.push(st);
			   }
		    }
	   }
	   this.onpList.add(new Struct('=',0.00));
}

/* funkcja oczyszczająca listy i stos */	
private void clearAbstractElements(){
	while(!this.stack.empty()){
			 this.stack.pop();
	 }
	this.list.clear();
	this.onpList.clear();
    this.iterator = 0;
}


/* sprawdzenie poprawności grafu do narysowania */   
public void initGraph(Graphics2D g2d){
	      if(!t2.isVisible()){this.t2.setVisible(true);this.allowGraph = true;}
		  if(this.pattern!=null && this.pattern.length()!=0){
			if(parsePattern()){
				if(this.allowGraph){
					clearAbstractElements();
					toList();
					toOnpList();
					if(this.allowIntegral){
						try{
						 drawIntegral(g2d);
						}catch(Exception x){
							//TODO tu musze obsłużyć wyjątek :)
						}
					}
					try{
						this.allowIntegral = false;
						this.drawGraph(g2d);
						g2d.setColor(Color.BLACK);
					//	g2d.drawString(this.pattern,1,10);
					 }catch(Exception exc){
						this.excFlag = true;
						this.allowCorSys = false;				  
					    g2d.setColor(Color.RED);
						g2d.drawString("Błąd",30,110);
						g2d.drawString("Program nie był w stanie",30,130);			// tu jestemmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
						g2d.drawString("wygenerować wykresu",30,150);		
						this.t2.setVisible(false);
					 }
				}
			}else{ 
				 this.allowCorSys = false;
				 g2d.setColor(Color.RED);
				 g2d.drawString("Wprowadzono niedozwolone znaki",1,110);
				 g2d.drawString("lub nieprawidłowa konstrukcja",1,130);
				 g2d.drawString("wyrażenia! ",1,150);
				 this.t2.setVisible(false);
		  }
	   }
}
		 
/* Funkcja rysuje wykres */
private void drawGraph(Graphics2D g2d){
			if(!this.isPatternOnList()){
			 this.generatePoints(g2d);
			}
			this.drawAllGraphs(g2d);
}

private void generatePoints(Graphics2D g2d ){
	GraphPoints gp;
	gp=new GraphPoints(this.pattern,CoorSys.CARTESIAN,new Color((int)(Math.random() * 0x1000000)));
	this.t2.addItem(gp.getPattern());
	this.t2.setSelectedItem(gp.getPattern());
	this.graphs.getGraphlist().add(gp);
	int prevX,prevY,nextX,nextY,tmpX,tmpY;
	double x , y,r, fi, counter;
	y = 0.0000;
	 try{
	  delta = Double.parseDouble(panelRight.DELTA.getText());
	 }catch(Exception exc){
	  delta = 0.001;
	  panelRight.DELTA.setText("0.001");
	 }
       if((delta>0.8)||(delta<0.0001)){
		 delta = 0.001;
		 panelRight.DELTA.setText("0.001");
        }
       
	 counter= fullWidth/delta;    
     x=-halfWidth;
     prevX = -2;
     prevY = -2;		
     gp.setSystem(CoorSys.CARTESIAN);
	 for(int i = 0 ; i<=counter ; i++){
          y=getResult(x);
          y =halfHeight-(y*scaleY);
          nextY = (int) Math.round(y);
          y = halfWidth +(x*scaleX);
          nextX =(int) Math.round(y);  
   	      tmpX = nextX;
   	      tmpY = nextY;
         if((prevY > nextY)&&((prevY-nextY)>200)){
        	   nextX = prevX;
        	   nextY = prevY;
        	   }
         if((prevY < nextY)&&((nextY-prevY)>200)){
      	   prevX = nextX;
      	   prevY = nextY;
      	   }
		 gp.getPoints().add(new Point(nextX,nextY));
		 prevX = tmpX;
		 prevY=  tmpY; 
		 x=x+delta;    
     }
	 // obliczenia biegunowe
    try{
		 Period = Double.parseDouble(panelRight.MAXFI.getText());
		 }catch(Exception exc){
		  Period = 12;
		  panelRight.MAXFI.setText("12");
		}
        if((Period>100)||(Period<0)){
			Period = 12;
			panelRight.MAXFI.setText("12");	
        }
    gp.setSystem(CoorSys.POLAR);
	counter = (Period*Math.PI)/delta;
	g2d.setColor(Color.ORANGE);
  	prevX = 0;
	prevY = 0;
	fi = delta;	
      for(int i=0;i<=counter;i++){
          r=getResult(fi); 
          y = r* Math.sin(fi);
          x = r * Math.cos(fi);
          y =halfHeight-(y*scaleY);
          nextY = (int) Math.round(y);
          y = halfWidth +(x*scaleX);
          nextX =(int) Math.round(y);  
          if(i==0){
          	prevX = nextX;
        	prevY = nextY;
          }
          //TODO wstawić też tu listy
         gp.getPolarpoints().add(new Point(nextX,nextY));
		 prevX = nextX;
		 prevY=  nextY; 
		 fi=fi+delta;    
	  }
}

public void drawAllGraphs(Graphics2D g2d){
	System.out.println("rysuje");
	ArrayList<Point> poin;
	int index=0;
	
	if(this.system.equals(CoorSys.CARTESIAN)){
		for(GraphPoints points : this.graphs.getGraphlist()){
			g2d.setColor(points.getColor());
			poin = points.getPoints();
			for(index = 0; index<poin.size()-1;index=index+2){
				g2d.drawLine(
								(int)poin.get(index).getX(),
								(int)poin.get(index).getY(),
								(int)poin.get(index+1).getX(),
								(int)poin.get(index+1).getY()
							);
			}
		}
	}else{
		for(GraphPoints points : this.graphs.getGraphlist()){
			g2d.setColor(points.getColor());
			poin = points.getPolarpoints();
			//TODO pomijanie równań niwygodnych dla systemu biegunowego 
			if(this.skipEquation(points)){return;}
			for(index = 0; index<poin.size()-1;index=index+2){
				g2d.drawLine(
								(int)poin.get(index).getX(),
								(int)poin.get(index).getY(),
								(int)poin.get(index+1).getX(),
								(int)poin.get(index+1).getY()
							);
			}
		}
		
  }
  
}
/**
 * Metoda ma za zadanie wyszukiwanie funkcji zabronionych we wzorach funkcji
 * @param p
 * 		   graf do sprawdzenia
 * @return
 * 			zwraca true jeżeli znaleziono funkcje zabronioną
 */
private boolean skipEquation(GraphPoints p) {
	String [] search = {"e^x","e^(x"};  // czarna lista 
    int index = 0;;
    for(String s: search){
    	index = this.pattern.indexOf(s);
    	if(index != -1){
        	return true;
    	}
    }
    return false;
}

public void clearAll(){
	this.graphs.clearAll();
	this.t2.removeAllItems();
}

public void changeSystem(){
	if(this.system.equals(CoorSys.POLAR)){
	 this.system = CoorSys.CARTESIAN;	
	 this.panelRight.SystemNameLabel.setText("Cartesian");
	 this.buttonPanel.Label1.setText("f(x)=");
	 this.panelRight.TLabel.setVisible(false);
	 this.panelRight.PiLabel.setVisible(false);
	 this.panelRight.MAXFI.setVisible(false);
	 this.panelRight.FakeLabel.setVisible(true);
	 this.panelRight.IntegralButton.setEnabled(true);
	 this.panelRight.LowIntegralLim.setEditable(true);
	 this.panelRight.HighIntegralLim.setEditable(true);
	 this.panelRight.xlabel.setText("X :");
	 this.panelRight.ylabel.setText("Y :");
	}else{
	 this.system = CoorSys.POLAR;
	 this.panelRight.SystemNameLabel.setText("Polar");
	 this.buttonPanel.Label1.setText("r  = ");
	 this.panelRight.TLabel.setVisible(true);
	 this.panelRight.PiLabel.setVisible(true);
	 this.panelRight.MAXFI.setVisible(true);
	 this.panelRight.FakeLabel.setVisible(false);
	 this.panelRight.IntegralButton.setEnabled(false);
	 this.panelRight.LowIntegralLim.setEditable(false);
	 this.panelRight.HighIntegralLim.setEditable(false);
	 this.panelRight.xlabel.setText("R :");
	 String str = "\u03D5";
	 this.panelRight.ylabel.setText(str+" :");
	}	
}
     
public String calcIntegral(double l, double h){
	double x, delta;
	double result = 0.000;      
  	 GraphPoints gp = this.getSelectedGraph();
  	 this.insertPattern(gp.getPattern());
  	 this.toList();
  	 this.toOnpList();
	delta = 0.00001;
	x = l;
	if(l<=h){
	 	while(x<h){
	      result = result + (getResult(x)*delta);
	 	  x = x+delta;   	 
	 	 }
	}else{
		 while(x>h){
			  result = result - (getResult(x)*delta);
		  x = x-delta;   
		 }		
	}
    this.LowIntegralLim = l;
    this.HighIntegralLim = h;
    this.allowIntegral = true;
    this.repaint();
	return this.dF.format(result);
}

/* Funkcje słuchaczy zdarzeń */     
	  @Override 
	  public void mouseMoved(MouseEvent e)
	      {
		     double x = 0.00;
			 /* pobranie i obróbka współrzędnych */ 
			  if(system.equals(CoorSys.CARTESIAN)){
				 if(this.panelRight!= null){	  
					 this.COORXPOS=e.getX();
					 this.COORYPOS=e.getY();
					 this.COORXPOS= (double)(this.COORXPOS - this.halfWidth)/this.scaleX;
					 this.SX = this.dF.format(this.COORXPOS);
					 this.panelRight.COORX.setText(SX);	 	
					 this.COORYPOS= (double)(this.halfHeight - this.COORYPOS)/this.scaleY;
					 this.SY =this.dF.format(this.COORYPOS);
					 this.panelRight.COORY.setText(SY);
				}
			  }else{
				if(this.panelRight!= null){	  
				    this.COORXPOS=e.getX();
					this.COORYPOS=e.getY();
					this.COORXPOS= (double)(this.COORXPOS - this.halfWidth)/this.scaleX;	
					this.COORYPOS= (double)(this.halfHeight - this.COORYPOS)/this.scaleY;
					x = COORXPOS;
					this.COORXPOS = Math.sqrt(Math.pow(COORXPOS,2)+Math.pow(COORYPOS,2)); // x -> r
					if((x>0) && (COORYPOS>=0)){
						this.COORYPOS =Math.atan(COORYPOS/x) ;// Y -> fi	
					}else if((x>0) && (COORYPOS<0)){
						this.COORYPOS =Math.atan(COORYPOS/x)+(2*Math.PI);
					}else if(x<0){
						this.COORYPOS =Math.atan(COORYPOS/x)+Math.PI;
					}else if((x == 0.00) && (COORYPOS>0)){
						this.COORYPOS = Math.PI/2;
					}else if ((x == 0.00) && (COORYPOS<0)){
						COORYPOS = (3*Math.PI)/2;
					}
					 
					this.SX = this.dF.format(this.COORXPOS);
					this.panelRight.COORX.setText(SX);	 
					this.SY =this.dF.format(this.COORYPOS);
					this.panelRight.COORY.setText(SY);
				}  
			  }
		  }
/**
 * Metoda sprawdza czy wzór funkcji jest na liście
 * @return
 * 		Zwraca true jeżeli tak
 */
private boolean isPatternOnList(){
	for(GraphPoints g: graphs.getGraphlist()){
		if(g.getPattern().equals(this.pattern)){
			return true;
		}
	}
	return false;
}

/**
 * 
 * @return
 * 		Zwraca graf zaznaczony w polu wyboru
 */
private GraphPoints getSelectedGraph(){
	for(GraphPoints gp : this.graphs.getGraphlist()){
		if(gp.getPattern().equals(this.t2.getSelectedItem())){
			return gp;
		}
	}
	return null;
}
	    
	  @Override
	  public void mouseDragged(MouseEvent e){
		  
		  }
	 /* metoda wywoływana gdy kursor opuszcza nasłuchiwany obiekt*/
	  @Override
	  public void mouseExited(MouseEvent e){
		    if(this.panelRight!= null){
			 this.panelRight.COORX.setText("0.00");
			 this.panelRight.COORY.setText("0.00");
		    }
		  }
	//metoda wywoływana, gdy następuje kliknięcie, czyli wciśnięcie i zwolnienie przycisku myszy, ale uwaga, obie te operacje muszą zajść w jednym położeniu.
	@Override
	public void mouseClicked(MouseEvent event){}
	 
	//metoda wywoływana, gdy zostaje wciśnięty przycisk myszy
	@Override
	public void mousePressed(MouseEvent event){}
	 
	//metoda wywoływana, gdy następuje zwolnienie przycisku myszy
	@Override
	public void mouseReleased(MouseEvent event){}
	 
	//metoda wywoływana, gdy kursor pojawia się w obszarze nasłuchującym na zdarzenia, na przykład panelu
	@Override
	public void mouseEntered(MouseEvent e){

		
		}

}