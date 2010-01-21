package org.opennms.sms.monitor.internal.config;

import static org.opennms.sms.reflector.smsservice.MobileMsgResponseMatchers.and;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.sms.reflector.smsservice.MobileMsgResponseMatcher;

public abstract class MobileSequenceResponse extends MobileSequenceOperation {

	private List<SequenceResponseMatcher> m_matchers = Collections.synchronizedList(new ArrayList<SequenceResponseMatcher>());
	
	private MobileSequenceTransaction m_transaction;

	public MobileSequenceResponse() {
		super();
	}
	
	public MobileSequenceResponse(String label) {
		super(label);
	}

	public MobileSequenceResponse(String gatewayId, String label) {
		super(gatewayId, label);
	}
	
	@XmlElementRef
	public List<SequenceResponseMatcher> getMatchers() {
		return m_matchers;
	}
	
	public void setMatchers(List<SequenceResponseMatcher> matchers) {
		m_matchers.clear();
		m_matchers.addAll(matchers);
	}
	
	public void addMatcher(SequenceResponseMatcher matcher) {
		m_matchers.add(matcher);
	}
	
	@XmlTransient
    public MobileSequenceTransaction getTransaction() {
        return m_transaction;
    }

    public void setTransaction(MobileSequenceTransaction transaction) {
        m_transaction = transaction;
    }

    public MobileMsgResponseMatcher getResponseMatcher(Properties session) {
		List<MobileMsgResponseMatcher> matchers = new ArrayList<MobileMsgResponseMatcher>();
		matchers.add(getResponseTypeMatcher());
		
		for (SequenceResponseMatcher m : getMatchers()) {
			matchers.add(m.getMatcher(session));
		}
		
		return and(matchers.toArray(new MobileMsgResponseMatcher[0]));
	}

	abstract protected MobileMsgResponseMatcher getResponseTypeMatcher();

	public String toString() {
        return new ToStringBuilder(this)
            .append("gatewayId", getGatewayId())
            .append("label", getLabel())
            .append("matchers", getMatchers())
            .toString();
    }

}
