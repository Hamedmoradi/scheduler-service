//package ir.baam.component;
//
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.server.Session;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.web.bind.support.SessionStatus;
//
//import javax.transaction.Transaction;
//import java.io.IOException;
//
//public class SessionConfirmationJob extends QuartzJobBean {
//    @Autowired
//    private SessionService sessionService;
//    @Autowired
//    private TransactionService transactionService;
//    @Autowired
//    private SystemLogger systemLogger;
//    public static final String TOKEN = "token";
//    private SpringInjectQuartzJobBean springInjectQuartzJobBean;
//
//    public SessionService getSessionService() {
//        return sessionService;
//    }
//
//    public void setSessionService(SessionService sessionService) {
//        this.sessionService = sessionService;
//    }
//
//    public TransactionService getTransactionService() {
//        return transactionService;
//    }
//
//    public void setTransactionService(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }
//
//    public void setSpringInjectQuartzJobBean(SpringInjectQuartzJobBean springInjectQuartzJobBean) {
//        this.springInjectQuartzJobBean = springInjectQuartzJobBean;
//    }
//
//    public SystemLogger getSystemLogger() {
//        return systemLogger;
//    }
//
//    public void setSystemLogger(SystemLogger systemLogger) {
//        this.systemLogger = systemLogger;
//    }
//
//    @Override
//    protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
//        springInjectQuartzJobBean = new SpringInjectQuartzJobBean();
//        springInjectQuartzJobBean.injectQuartzJobToSpringApplicationContext(this);
//        String token = paramJobExecutionContext.getMergedJobDataMap().getString(TOKEN);
//        Session session = sessionService.getByToken(token);
//        if (session != null) {
//            if (session.getPaymentConfirmation() == null || session.getPaymentConfirmation() != true) {
//                Transaction transactionToBeRolledBack = transactionService.getRollBackTransactionOfPayment(session);
//                if (transactionToBeRolledBack != null) {
//                    try {
//                        transactionService.rollBackTransaction(transactionToBeRolledBack);
//                    } catch (IOException e) {
//                        systemLogger.logException("Exception while rolling back transaction", e);
//                    }
//                    session = sessionService.getByToken(token);
//                    session.setStatus(SessionStatus.FI);
//                    session.setPaymentConfirmation(false);
//                    sessionService.saveOrUpdate(session);
//                }
//            }
//        }
//    }
//}