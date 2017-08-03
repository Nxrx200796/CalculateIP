package com.example.naru.testip;

import android.widget.Toast;

import java.util.Scanner;

public class EIP {
    public Integer[] _ip = new Integer[ 4 ] ;
    public int _class ;
    public int _bits ;
    public int nextbits ;
    public boolean nature ; // true subnet and false net
    public Integer[ ] idnet = new Integer[ 4 ] ;
    public Integer[ ] broadcast = new Integer[ 4 ] ;
    public Integer[ ] _mask = new Integer[ 2 ] ;
    public String ip ;
    public final Integer[] decbit = { 1 , 2 , 4 , 8 , 16 , 32 , 64 , 128 } ;
    //1 error struct ip
    //2 error no text
    public EIP() {
        for( int i = 0 ; i < 4 ; i ++ ) {
            _ip[ i ] = idnet[ i ] = 0 ;
            broadcast[ i ] = 255 ;
        }
        nextbits = _bits = _class = _mask[ 0 ] = _mask[ 1 ] = 0 ;
        nature = true ;
        ip = "" ;
    }
// 11111111 11111111 11111111 11111111

    String getTypeNet() {
        String type = _class == 1 ? "A" : _class == 2 ? "B" : "C" ;
        boolean mode = _ip[ 0 ] == 10 ;
        mode |= _ip[ 0 ] == 172 && _ip[ 1 ] > 15 && _ip[ 1 ] < 32  ;
        mode |= _ip[ 0 ] == 192 && _ip[ 1 ] == 68 ;
        //Este último rango se destina al "direccionamiento automático de IPs privadas"
        //(APIPA). Se activa cuando falla el mecanismo normal para asignarlas.
        mode |= _ip[ 0 ] == 169 && _ip[ 1 ] == 254 ;
        return "Class " + type + " - IP " + ( mode ? "Private" : "Public" ) ;
    }

    String getNroHost() {
        int e = 32 - _bits ;
        long fi = 1 ;
        for( int i = 0 ; i < e ; i ++ ) fi *= 2 ;
        return fi + "" ;
    }

    String getLastHost() {
        return broadcast[ 0 ] + "." + broadcast[ 1 ] + "." + broadcast[ 2 ] + "." + (broadcast[ 3 ] - 1 ) ;
    }

    String getFirstHost() {
        return idnet[ 0 ] + "." + idnet[ 1 ] + "." + idnet[ 2 ] + "." + (idnet[ 3 ] + 1 ) ;
    }

    String getBroadcast() {
        String _broadcast = broadcast[ 0 ] + "";
        for( int i = 1 ; i < 4 ; i ++ ) _broadcast += "." + broadcast[ i ] ;
        return _broadcast ;
    }

    String getIdNet() {
        for( int i = 0 ; i < 4 ; i ++ ) idnet[ i ] = broadcast[ i ] = _ip[ i ] ;
        if( !nature ) {
            int reason = 256 - _mask[ 1 ] ;
            idnet[ _mask[ 0 ] ] = _ip[ _mask[ 0 ] ] - _ip[ _mask[ 0 ] ] % reason ;
            broadcast[ _mask[ 0 ] ] = idnet[ _mask[ 0 ] ] + reason - 1 ;
            for( int i = _mask[ 0 ] + 1 ; i < 4 ; i ++ ) idnet[ i ] = 0 ;
            for( int i = _mask[ 0 ] + 1 ; i < 4 ; i ++ ) broadcast[ i ] = 255 ;
        } else {
            for( int i = _mask[ 0 ] ; i < 4 ; i ++ ) idnet[ i ] = 0 ;
            for( int i = _mask[ 0 ] ; i < 4 ; i ++ ) broadcast[ i ] = 255 ;
        }
        String _idnet = idnet[ 0 ] + "";
        for( int i = 1 ; i < 4 ; i ++ ) _idnet += "." + idnet[ i ] ;
        return _idnet ;
    }

    String getMask() {
        String mask = "255." ;
        if( !nature ) {
            nextbits = _bits - ( _class == 1 ? 8 : _class == 2 ? 16 : 24 ) ;
            _mask[ 0 ] = _bits > 24 ? 3 : _bits > 16 ? 2 : 1 ;
            _mask[ 1 ] = ( nextbits % 8 == 0 ) ? 255 : sumrange( nextbits % 8  ) ;
            mask += _mask[ 0 ] == 1 ? _mask[ 1 ] + ".0.0" : _mask[ 0 ] == 2 ? "255."+ _mask[ 1 ] + ".0" : "255.255."+ _mask[ 1 ] ;
        } else {
            mask += _class == 1 ? "0.0.0" : _class == 2 ? "255.0.0" : "255.255.0" ;
            _mask[ 0 ] = _class ;
        }
        return mask ;
    }

    int sumrange( int a ) {
        int fi = 0 ;
        for( int i = 8 - a ; i < 8 ; i++ ) fi += decbit[ i ] ;
        return fi ;
    }

    String getIpBits() {
        return ip  + "/" + _bits ;
    }

    String getNature() {
        if( _bits == 0 ) {
            _bits = _class == 1 ? 8 : _class == 2 ? 16 : 24 ;
        }
        nature = _class == 1 && _bits == 8 || _class == 2 && _bits == 16 || _class == 3 && _bits == 24 ;
        return nature ? "Net" : "SubNet" ;
    }
    boolean evalip( String ip ,  String bits ) {
        if(  valip( ip ) ) {
            this.ip = ip ;
            get_class();
            return evalbits( bits ) ;
        }
        return false ;
    }

    void get_class() {
        _class = _ip[ 0 ] > 191 ? 3 : _ip[ 0 ] > 127 ? 2 : 1 ;
    }

    boolean evalbits( String bits ) {
        if( evalnum( bits , 2 )) {
            return _class == 1 && _bits > 7 || _class == 2 && _bits > 15 || _class == 3 && _bits > 23  ;
        }
        return false ;
    }

    boolean valip( String ip ) {
        int len = ip.length() ;
        int ind = 0 ;
        String carry = "" + ip.charAt( 0 ) ;
        for( int i = 1 ; i < len ; i ++ )  {
            if( ip.charAt( i ) != '.' ) {
                carry += ip.charAt( i ) ;
            } else {
                if( evalnum( carry , 1 ) && i < len - 1 && ind < 4 ) {
                    _ip[ ind ] = Integer.parseInt( carry ) ;
                    ind ++ ;
                    carry = ip.charAt( i + 1 ) + ""  ;
                    i ++ ;
                } else {
                    return false ;
                }
            }
        }
        if( _ip[ 0 ] == 0 ) return  false ;
        if( ind == 3 && evalnum( carry , 1 )) {
            _ip[ ind ] = Integer.parseInt( carry ) ;
            return true ;
        }
        return false ;
    }

    boolean evalnum( String s , int _case ) {
        try{
            int ev = Integer.parseInt( s ) ;
            if( _case == 1 && ev > -1 && ev < 256 ) {
                return true ;
            } else if( _case == 2 && ev > -1 && ev < 33  ) {
                if( ev == 0 ) _bits = _class == 1 ? 8 : _class == 2 ? 16 : 24 ;
                else _bits = ev ;
                return true ;
            }
            return false ;
        }catch( Exception e) {
            return false ;
        }
    }

    boolean valrange( int n , int left , int rigth ) {
        return n >= left && n <= rigth ;
    }

    public String getBits(){
        return _bits + "" ;
    }
}