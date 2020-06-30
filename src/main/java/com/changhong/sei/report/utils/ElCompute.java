package com.changhong.sei.report.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * @desc：el表达式运算
 * @author：zhaohz
 * @date：2020/6/30 16:09
 */
public class ElCompute {
	private Stack<Object> dataStack=new Stack<Object>();
	private Stack<Character> operateStack=new Stack<Character>();
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		String expr="22/2*2-(5+(1*2))-2*2";
		for(int i=0;i<1;i++) {
			com.bstek.ureport.utils.ElCompute el=new com.bstek.ureport.utils.ElCompute();
			Object data=el.doCompute(expr);
			System.out.println(data);
		}
		long end=System.currentTimeMillis();
		System.out.println(end-start);
	}
	
	public Object doCompute(String expr) {
		init(expr);
		return dataStack.pop();
	}
	private void init(String expr){
		StringBuilder dataSb=new StringBuilder();
		char prevQuote=' ',prevChar=' ';
		for(int i=0;i<expr.length();i++){
			char c=expr.charAt(i);
			if(prevChar=='\\') {
				dataSb.append(c);
				prevChar=c;
				continue;
			}
			if(prevQuote=='"') {
				if(c=='"') {
					prevQuote=' ';
					dataStack.push(dataSb.toString());
					dataSb.setLength(0);
				}else {					
					dataSb.append(c);
				}
				prevChar=c;
				continue;
			}
			switch(c){
			case '+':
				doOp(dataSb, c,prevChar);
				break;
			case '-':
				doOp(dataSb, c,prevChar);
				break;
			case '*':
				doOp(dataSb, c,prevChar);
				break;
			case '/':
				doOp(dataSb, c,prevChar);
				break;
			case '%':
				doOp(dataSb, c,prevChar);
				break;
			case '(':
				operateStack.push(c);
				break;
			case ')':
				addDataStack(dataSb);
				doCalculate(1);
				break;
			case '"':
				if(prevQuote=='"'){
					prevQuote=' ';
					dataStack.push(dataSb.toString());
					dataSb.setLength(0);
				}else{
					prevQuote='"';
				}
				break;
			case ' ':
				if(prevQuote=='"') {
					dataSb.append(c);
				}
				break;
			default:
				dataSb.append(c);
			}
			prevChar=c;
		}
		if(dataSb.length()>0){
			addDataStack(dataSb);
		}
		doCalculate(0);
	}
	
	private void doOp(StringBuilder dataSb,char op,char prevChar) {
		if(dataSb.length()==0 && prevChar!=')' && prevChar!='\"'){
			dataSb.append(op);
		} else {
			addDataStack(dataSb);
			if(op=='+' || op=='-') {
				doCalculate(0);
			}else {
				doCalculate(2);				
			}
			operateStack.push(op);
		}
	}
	
	private void doCalculate(int current) {
		if(operateStack.empty()){
			return;
		}
		char prevOp=operateStack.peek();
		if(prevOp=='('){
			operateStack.pop();
			return;
		}
		if(current==0 || current==1){
			char op=operateStack.pop();
			do{				
				Object right=dataStack.pop();
				Object left=dataStack.pop();
				Object result=calculate(left, op, right);
				dataStack.push(result);
				if(operateStack.isEmpty()){
					break;
				}
				op=operateStack.pop();
			}while(op!='(');
		}else if(current==2){
			while(prevOp=='*' || prevOp=='/' || prevOp=='%') {
				Object right=dataStack.pop();
				Object left=dataStack.pop();
				char op=operateStack.pop();
				Object result=calculate(left, op, right);
				dataStack.push(result);
				if(operateStack.isEmpty()){
					break;
				}
				prevOp=operateStack.peek();
				if(prevOp=='('){
					break;
				}
			}
		}
	}
	
	private Object calculate(Object left,char op,Object right){
		if((op=='*' || op=='/' || op=='%' || op=='-')){
			if(right instanceof String || left instanceof String){
				return left.toString()+op+right.toString();
			}
			BigDecimal b1=(BigDecimal)left;
			BigDecimal b2=(BigDecimal)right;
			if(op=='*'){
				return b1.multiply(b2);
			}else if(op=='/'){
				return b1.divide(b2,10,RoundingMode.HALF_UP).stripTrailingZeros();
			}else if(op=='%'){
				return b1.divideAndRemainder(b2)[1];
			}else if(op=='-'){
				return b1.subtract(b2);
			}
		}else if(op=='+'){
			if(right instanceof String || left instanceof String){
				return left.toString()+right.toString();
			}else{
				BigDecimal b1=(BigDecimal)left;
				BigDecimal b2=(BigDecimal)right;
				return b1.add(b2);
			}
		}
		throw new RuntimeException("Unkown operate "+op+"");
	}
	

	private void addDataStack(StringBuilder dataSb) {
		if(dataSb.length()==0)return;
		String data=dataSb.toString();
		dataSb.setLength(0);
		try{
			BigDecimal bd= Utils.toBigDecimal(data);
			dataStack.push(bd);
		}catch(Exception ex){
			dataStack.push(data);
		}
	}
}
