<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="edu.pitt.bank.Transaction"%>
<%@page import="edu.pitt.bank.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //check to ensure user is logged in
    if(session.getAttribute("authenticatedUser") == null && session.getAttribute("selectedAccount") == null){
        session.setAttribute("serverMessage", "Access denied. If you were previously logged in, please login again.");
        response.sendRedirect("index.jsp");//redirect to login page
    }else{
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Account selectedAccount = (Account) session.getAttribute("selectedAccount");

%>
    <!DOCTYPE html>
    <html lang="en">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link rel="stylesheet" href="css/bootstrap.css" type="text/css" media="screen" title="no title" charset="utf-8">
            <title>Transaction History</title>
        </head>
        <body>
            <div class="container">
            <h2>Transaction history</h2>
            <table id="tblTransactions" class="table table-striped table-bordered">
                <thead>
                    <th class="info">Transaction Type</th>
                    <th class="info">Date/Time</th>
                    <th class="info">Amount</th>
                </thead>
                <tbody>
                    <%
                        try{
                           for(Transaction t: selectedAccount.getTransactions()){
                            out.print("<tr>");
                            out.print("<td>" + t.getType() + "</td>");
                            out.print("<td>" + dateFormat.format(t.getTransactionDate()) + "</td>");
                            out.print("<td>" + moneyFormat.format(t.getAmount()) + "</td>");
                            out.print("</tr>");
                            } 
                        }catch(NullPointerException e){
                            out.print("No transactions found.");
                        }
                        
                    %>
                </tbody>
            </table>
            </div>
        </body>
    </html>
<%
    }
%>
    

