package com.dam.m21.petsaway.alertas_adoptar;

import android.os.Parcel;
import android.os.Parcelable;

public class Fotos implements Parcelable {
	String url;

	public Fotos(String url) {
		this.url = url;
	}

	public Fotos() {
	}

	protected Fotos(Parcel in) {
		url = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Fotos> CREATOR = new Creator<Fotos>() {
		@Override
		public Fotos createFromParcel(Parcel in) {
			return new Fotos(in);
		}

		@Override
		public Fotos[] newArray(int size) {
			return new Fotos[size];
		}
	};

	public String getUrl() {
		return url;
	}
}
