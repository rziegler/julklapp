package ch.bbv.julklapp.dto.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.google.appengine.api.datastore.Email;

public class EmailAdapter extends XmlAdapter<String, Email> {

	@Override
	public String marshal(Email value) throws Exception {
		return value.getEmail();
	}

	@Override
	public Email unmarshal(String value) throws Exception {
		return new Email(value);
	}

}
