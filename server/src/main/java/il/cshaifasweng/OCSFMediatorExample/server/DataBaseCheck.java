package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.server.FactoryUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DataBaseCheck {
    public boolean isDatabaseNotEmpty() {
        Session session = FactoryUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
            Root<?> root = criteriaQuery.from(Task.class);
            criteriaQuery.select(builder.count(root));
            Query<Long> query = session.createQuery(criteriaQuery);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();

        }
    }
}
