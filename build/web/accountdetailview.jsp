<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="edu.pitt.utilities.ErrorLogger"%>
<%@page import="edu.pitt.bank.Account"%>
<%@page import="edu.pitt.bank.Bank"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.pitt.bank.Customer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  
    //Begin authentation conditional
    if(session.getAttribute("authenticatedUser") == null){
        session.setAttribute("serverMessage", "Access denied. If you were previously logged in, please login again.");
        response.sendRedirect("index.jsp");//redirect to login page
    }else{
        //initialize attributes and params
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        Customer sessionUser = (Customer) session.getAttribute("authenticatedUser");
        ArrayList<String>permissions = (ArrayList<String>) session.getAttribute("userPermissions");
        Bank wncBank = null;
        Account currentAccount = null;
        String selectedAccount ="";// = (String) session.getAttribute("currentAccountID");
        final double MAX_DEPOSIT = 10000.00;
                
        if(request.getParameter("cboAccountList") != null){
            selectedAccount = request.getParameter("cboAccountList");
            if(selectedAccount != null && !(selectedAccount.equals(""))){
                currentAccount = new Account(selectedAccount);
            }
        }
        
        //check to see if user is performing a deposit or withdraw
        if(request.getParameter("hdnActionType") != null && currentAccount != null && request.getParameter("txtAmount")!= null) {
            try{
                String actionType = (String) request.getParameter("hdnActionType");
                String amt = (String)request.getParameter("txtAmount");
                
                if(!amt.equals("")){
                    double amount = Double.parseDouble(amt);
                    if(actionType.equals("deposit")){
                        
                        if(amount >= MAX_DEPOSIT){
                            out.print("<script> alert('WARNING: When you deposit $10,000 or more in cash at the bank, the IRS requires us to complete submit a Form 8300.');</script>");
                        }
                        currentAccount.deposit(amount);
  
                    }else if(actionType.equals("withdraw")){
                        if(currentAccount.getStatus().equals("active")){
                            if(amount <= currentAccount.getBalance()){
                                currentAccount.withdraw(amount);
                            }else{
                                out.print("<script> alert('Insufficient funds!');</script>");
                            }
                        }else{
                            out.print("<script> alert('This account not active!');</script>");
                        }  
                    }
                }
            }catch(NumberFormatException e){
                System.out.println("Warning: " + e);
            }   
            
        }
        
        //create the bank class if it doesn't exist
        if (session.getAttribute("bank")==null){
            wncBank = new Bank();
        }else{
            wncBank = (Bank) session.getAttribute("bank");
        }
        
        //populate the account list combo box
        ArrayList<Account> customerAccounts = wncBank.getCustomerAccounts(sessionUser.getCustomerID());
        
        
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.css" type="text/css" media="screen" title="no title" charset="utf-8">
        <title>Account Home</title>
        <script src="js/scripts.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container">
            <br/>
            <form class="pull-right" id="frmLogout" name="frmLogout" action="logout.jsp" method="post">
                <input type="submit" name="btnLogout" id="btnLogout" value="Logout"/>
            </form>
            <div class="page-header">
                <h2>Welcome to WNC Bank Online</h2>
            </div>
            
            <p class="lead"><% out.print(getWelcomeMessage(sessionUser, permissions)); %></p>
            <form class="form-horizontal" id="frmAccountList" name="frmAccountList" action="accountdetailview.jsp" method="POST">
                <label for="cboAccountList">Your accounts: </label>
                <select class="form-control" id="cboAccountList" name="cboAccountList" onChange="submitOnSelect();">
                    <option value="">-- Please select account --</option>
                    <%
                        //fill in the combo box with account ids
                        for(Account a: customerAccounts){
                            String accountName = a.getAccountID();

                            if(accountName.equals(selectedAccount)){
                                out.print("<option value ='" + accountName + "' selected > " + accountName + "</option>");
                            }else{
                                out.print("<option value ='" + accountName + "' > " + accountName + "</option>");
                            }
                        }
                    %>
                </select>
                <br/>
                <table class="table table-bordered table-condensed">
                    <thead>
                        <tr>
                            <th colspan="2" class="text-center info">Account Details</th>
                        </tr>
                    </thead>
                    <tbody>
                <%
                    if(currentAccount != null){
                        session.setAttribute("selectedAccount", currentAccount);
                        out.print("<tr><td>Account Type: </td><td>" + currentAccount.getType() + "</td></tr>");
                        out.print("<tr><td>Balance: </td><td>" + moneyFormat.format(currentAccount.getBalance()) + "</td></tr>");
                        out.print("<tr><td>Interest Rate: </td><td>" + percentFormat.format(currentAccount.getInterestRate()) + "</td></tr>");
                        out.print("<tr><td>Penalty: </td><td>" + moneyFormat.format(currentAccount.getPenalty()) + "</td></tr>");
                    }else{
                        out.print("<tr><td>--</td><td>--</td></tr>");
                    }   
                %>
                    </tbody>
                </table>
                <h4>Add Transaction</h4>
                <div class="form-inline">
                    <label class="sr-only" for="txtAmount">Amount</label>
                    <div class="input-group">
                        <div class="input-group-addon">$</div>
                        <input type="text" class="form-control" id="txtAmount" name="txtAmount" placeholder="Amount"/>
                    </div>

                    <input type="hidden" name="hdnActionType" id="hdnActionType">
                    <input type="button" class="btn btn-success" name="btnDeposit" id="btnDeposit" value="Deposit" onclick="deposit()"/>
                    <input type="button" class="btn btn-danger " name="btnWithdraw" id="btnWithdraw" value="Withdraw" onclick="withdraw()"/>
                </div>
            </form>
            
            <br/>
            
            <form class="form-horizontal" id="frmViewTransactions" name="frmAccountDetails" target="_blank" action="transactionview.jsp" method="POST">
                <input type="submit" class="btn btn-primary" name="btnSubmit" id="btnSubmit" value="Show Previous Transactions"/>       
            </form>
            
        </div>
    </body>
</html>
<%      
    }//end authentication else conditional
%>

<%!

    //functions: Normally would be in a controller or service.
    private String getWelcomeMessage(Customer accountOwner, ArrayList<String> permissions){
        String welcomeMsg = accountOwner.getFirstName() + " " + accountOwner.getLastName();
        welcomeMsg +=", you have the following permissions in this system: ";

        StringBuilder sb = new StringBuilder();
            for(String access: permissions){
                    sb.append(access + ", ");
            }

            if(sb.length()>0){
                    sb.setLength(sb.length() - 2);				
                    welcomeMsg += sb.toString();
            }else{
                    welcomeMsg += "none";
            }

        return welcomeMsg;
    }
    
%>