package com.wso2telco.services.dep.sandbox.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import com.wso2telco.services.dep.sandbox.dao.GenaricDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SenderAddress;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

class HibernateCommonDAO extends AbstractDAO implements GenaricDAO {

	{
		LOG = LogFactory.getLog(HibernateCommonDAO.class);
	}
	public boolean isWhiteListedSenderAddress(int userId, String shortCode){
		
		SenderAddress senderAddress = null;
		
		Session session = getSession();
		
		senderAddress = (SenderAddress) session.createQuery("from SenderAddress where user.id = ? and shortCode = ?").setInteger(0, userId).setString(1, shortCode).uniqueResult();
		if (senderAddress != null) {
			
            return true;
        }
		
		return false;
	}
	
	 public User getUser(String username) {

	        Session sess = getSession();
	        User usr = null;
	        try {
	            usr = (User) sess.createQuery("from User where userName = ?").setString(0, username).uniqueResult();
	            if (usr == null) {
	                throw new Exception("User Not Found");
	            }

	        } catch (Exception e) {
	           LOG.error("getUser",e);
	        } finally {
	           sess.close();
	        }

	        return usr;
	    }
	 
	 @SuppressWarnings("deprecation")
	public List<ManageNumber>  getWhitelisted(int userid, List numbers) {

			Session sess = getSession();

			List<ManageNumber> whitelisted = null;
			try {
				whitelisted =sess.createQuery("from ManageNumber where user.id = :userid and number  in(:numbers)").setParameter("userid", Integer.valueOf(userid))
						.setParameterList("numbers", numbers).list();
				/*query.setP
				query.setParameter("userid", userid);
				query.setParameterList( "numbers", numbers);
				whitelisted=query.list();
*/
			} catch (Exception e) {
				System.out.println("getUserWhitelist: " + e);
				LOG.error("getWhitelisted",e);
			} finally {
				//sess.close();
			}
			return whitelisted;
		}


	@Override
	public APITypes getAPIType(String api) {
	    Session sess = getSession();
		APITypes apis = null;
		try {
		    apis = (APITypes) sess.createQuery("from APITypes WHERE lower(apiname) = :api").setParameter("api", api).uniqueResult();

		} catch (Exception e) {
		    LOG.error("getAPITypes", e);
		} finally {
		    sess.close();
		}

		return apis;
	}

	@Override
	public APIServiceCalls getServiceCall(int apiId,String service) {
	    Session sess = getSession();
	    APIServiceCalls services = null;
		try {
		    services = (APIServiceCalls) sess.createQuery("from APIServiceCalls WHERE apiType.id = ? AND lower(serviceName) = ?")
			    .setParameter(0, apiId).setParameter(1, service).uniqueResult();

		} catch (Exception e) {
		    LOG.error("getServiceCall", e);
		} finally {
		    sess.close();
		}

		return services;
	}

	@Override
	public Attributes getAttribute(String attribute) {
	    Session sess = getSession();
	    Attributes attributeObj = null;
		try {
		    attributeObj = (Attributes) sess.createQuery("from Attributes WHERE lower(attributeName) = :attribute")
			    .setParameter("attribute", attribute).uniqueResult();

		} catch (Exception e) {
		    LOG.error("getAttribute", e);
		} finally {
		    sess.close();
		}

		return attributeObj;
	}

	@Override
	public AttributeDistribution getAttributeDistribution(int apiServiceId,
		int attributeId) {
	    Session sess = getSession();
	    AttributeDistribution distributionObj = null;
		try {
		    distributionObj = (AttributeDistribution) sess.createQuery("from AttributeDistribution WHERE attribute.attributeId = ? AND serviceCall.apiServiceCallId = ?")
			    .setParameter(0, attributeId).setParameter(1, apiServiceId).uniqueResult();

		} catch (Exception e) {
		    LOG.error("getServiceCall", e);
		} finally {
		    sess.close();
		}

		return distributionObj;
	}

	@Override
        public void saveAttributeValue(AttributeValues valueObj) {
    
    	try {
    	    saveOrUpdate(valueObj);
    	} catch (Exception e) {
    	    LOG.error("saveAttributeValue", e);
    	}
        }

	@Override
	    public AttributeValues getAttributeValue(AttributeDistribution distributionObj){
	    Session sess = getSession();
	    AttributeValues value = null;
	        try {
	            value = (AttributeValues) sess.createQuery("from AttributeValues where attributeDistributionId = ?").setParameter(0,distributionObj).uniqueResult();
	         } catch (Exception e) {
	           LOG.error("getAttributeValue",e);
	        } finally {
	           sess.close();
	        }

	        return value;
	}
}
