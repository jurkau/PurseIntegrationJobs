package integration;

import izly.Purse;
import izly.RejetTransactionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import secretCode.CodeSecret;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class PurseIntegrationTest {

    Purse purse;
    CodeSecret codeSecret;

    @Before
    public void setUp() throws RejetTransactionException {
        purse = Mockito.mock(Purse.class);
        Mockito.doThrow(new RejetTransactionException()).when(purse).debite(Mockito.anyDouble(), Mockito.anyString());
        Mockito.doNothing().when(purse).debite(Mockito.anyDouble(), Mockito.eq("9876"));
        Mockito.when(purse.getSolde()).thenReturn(50.0, 0.0);

        codeSecret = Mockito.mock(CodeSecret.class);
        Mockito.when(codeSecret.revelerCode()).thenReturn("9876");
    }

    @Test
    public void testDebite() throws Exception {
        String code = codeSecret.revelerCode();
        purse.credite(50);
        double solde = purse.getSolde();
        purse.debite(50, code);
        Assert.assertEquals(solde - 50, purse.getSolde(), 0);
    }

    @Test (expected = RejetTransactionException.class)
    public void testDebitRejetéSurCodeIncorrect() throws Exception {
        String code = codeSecret.revelerCode();
        purse.credite(50);
        purse.debite(30, altere(code));
    }

    private String altere(String code) {
        if (code.charAt(0)=='1')
            return code.replace('1', '2');
        return code.replace(code.charAt(0), '1');
    }
}
