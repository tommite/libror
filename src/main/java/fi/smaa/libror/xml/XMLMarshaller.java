package fi.smaa.libror.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.decision_deck.xmcda3.AlternativeType;
import org.decision_deck.xmcda3.AttributeType;
import org.decision_deck.xmcda3.BinaryValuedPairType;
import org.decision_deck.xmcda3.KeyedEntityReference;
import org.decision_deck.xmcda3.KeyedEntityType;
import org.decision_deck.xmcda3.ValuedPairType;
import org.decision_deck.xmcda3.uta.UtagmsInputDocument;
import org.decision_deck.xmcda3.uta.UtagmsInputDocument.UtagmsInput;

import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;

public class XMLMarshaller {
	
	public static RORModel xmlInputToRORModel(UtagmsInputDocument input) {
		UtagmsInput inp = input.getUtagmsInput();
		
		List<AlternativeType> lAlts = inp.getAlternatives().getAlternativeList();
		List<ValuedPairType> lPerf = inp.getPerformances().getValuedPairList();
		List<BinaryValuedPairType> lPref = inp.getPreferences().getValuedPairList();
		List<AttributeType> lAttr = createAttributeList(lPerf);

		// performances
		RealMatrix rm = new Array2DRowRealMatrix(lAlts.size(), lAttr.size());
		for (ValuedPairType p : lPerf) {
			int fromIndex = getKeyedEntityIndex(p.getFrom(), lAlts);
			int toIndex = getKeyedEntityIndex(p.getTo(), lAttr);
			rm.setEntry(fromIndex, toIndex, p.getMeasurement().getValue());
		}
		
		// preferences
		RORModel model = new RORModel(new PerformanceMatrix(rm));
		for (BinaryValuedPairType p : lPref) {
			int fromIndex = getKeyedEntityIndex(p.getFrom(), lAlts);
			int toIndex = getKeyedEntityIndex(p.getTo(), lAlts);
			model.addPreference(fromIndex, toIndex);
		}
		return model;
	}

	private static List<AttributeType> createAttributeList(List<ValuedPairType> lPerf) {
		Set<String> strs = new HashSet<String>();
		for (ValuedPairType p : lPerf) {
			strs.add(p.getTo().getRef());
		}
		List<AttributeType> aList = new ArrayList<AttributeType>();
		for (String s : strs) {
			AttributeType a = AttributeType.Factory.newInstance();
			a.setId(s);
			aList.add(a);
		}
		return aList;
	}

	private static int getKeyedEntityIndex(KeyedEntityReference to, List<? extends KeyedEntityType> lKeyedEnt) {
		String ref = to.getRef();
		for (int i=0;i<lKeyedEnt.size();i++) {
			if (lKeyedEnt.get(i).getId().equals(ref)) {
				return i;
			}
		}
		throw new IllegalStateException("unknown keyed entity");
	}
}
