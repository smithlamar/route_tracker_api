package com.lamarjs.route_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Prediction {
	String tmstmp; // Ex: "20170314 11:25"
	String typ; // Ex: "A"
	String stpnm; // Ex: "Michigan \u0026 Balbo"
	int stpid; // Ex: "1584"
	int vid; // Ex: "1315"
	int dstp; // Ex: 4833
	String rt; // Ex: "4"
	String rtdir; // Ex: "Northbound"
	String des; // Ex: "Illinois Center"
	String prdtm; // Ex: "20170314 11:32"
	String tablockid; // Ex: "4 -716"
	String tatripid; // Ex: "10001019"
	boolean dly; // Ex: false
	String prdctdn; // Ex: "7"
	String zone; // Ex: ""

	/**
	 * @return the tmstmp
	 */
	public String getTmstmp() {
		return tmstmp;
	}

	/**
	 * @param tmstmp
	 *            the tmstmp to set
	 */
	public void setTmstmp(String tmstmp) {
		this.tmstmp = tmstmp;
	}

	/**
	 * @return the typ
	 */
	public String getTyp() {
		return typ;
	}

	/**
	 * @param typ
	 *            the typ to set
	 */
	public void setTyp(String typ) {
		this.typ = typ;
	}

	/**
	 * @return the stpnm
	 */
	public String getStpnm() {
		return stpnm;
	}

	/**
	 * @param stpnm
	 *            the stpnm to set
	 */
	public void setStpnm(String stpnm) {
		this.stpnm = stpnm;
	}

	/**
	 * @return the stpid
	 */
	public int getStpid() {
		return stpid;
	}

	/**
	 * @param stpid
	 *            the stpid to set
	 */
	public void setStpid(int stpid) {
		this.stpid = stpid;
	}

	/**
	 * @return the vid
	 */
	public int getVid() {
		return vid;
	}

	/**
	 * @param vid
	 *            the vid to set
	 */
	public void setVid(int vid) {
		this.vid = vid;
	}

	/**
	 * @return the dstp
	 */
	public int getDstp() {
		return dstp;
	}

	/**
	 * @param dstp
	 *            the dstp to set
	 */
	public void setDstp(int dstp) {
		this.dstp = dstp;
	}

	/**
	 * @return the rt
	 */
	public String getRt() {
		return rt;
	}

	/**
	 * @param rt
	 *            the rt to set
	 */
	public void setRt(String rt) {
		this.rt = rt;
	}

	/**
	 * @return the rtdir
	 */
	public String getRtdir() {
		return rtdir;
	}

	/**
	 * @param rtdir
	 *            the rtdir to set
	 */
	public void setRtdir(String rtdir) {
		this.rtdir = rtdir;
	}

	/**
	 * @return the des
	 */
	public String getDes() {
		return des;
	}

	/**
	 * @param des
	 *            the des to set
	 */
	public void setDes(String des) {
		this.des = des;
	}

	/**
	 * @return the prdtm
	 */
	public String getPrdtm() {
		return prdtm;
	}

	/**
	 * @param prdtm
	 *            the prdtm to set
	 */
	public void setPrdtm(String prdtm) {
		this.prdtm = prdtm;
	}

	/**
	 * @return the tablockid
	 */
	public String getTablockid() {
		return tablockid;
	}

	/**
	 * @param tablockid
	 *            the tablockid to set
	 */
	public void setTablockid(String tablockid) {
		this.tablockid = tablockid;
	}

	/**
	 * @return the tatripid
	 */
	public String getTatripid() {
		return tatripid;
	}

	/**
	 * @param tatripid
	 *            the tatripid to set
	 */
	public void setTatripid(String tatripid) {
		this.tatripid = tatripid;
	}

	/**
	 * @return the dly
	 */
	public boolean isDly() {
		return dly;
	}

	/**
	 * @param dly
	 *            the dly to set
	 */
	public void setDly(boolean dly) {
		this.dly = dly;
	}

	/**
	 * @return the prdctdn
	 */
	public String getPrdctdn() {
		return prdctdn;
	}

	/**
	 * @param prdctdn
	 *            the prdctdn to set
	 */
	public void setPrdctdn(String prdctdn) {
		this.prdctdn = prdctdn;
	}

	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}
}
