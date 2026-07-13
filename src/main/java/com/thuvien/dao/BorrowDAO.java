    package com.thuvien.dao;


    import java.sql.Connection;

    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;


import java.sql.Statement;
    import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;

import com.thuvien.dto.BorrowViewDTO;

    public class BorrowDAO {
        public List<BorrowViewDTO> getAllBorrows(){
            List<BorrowViewDTO> list = new ArrayList<>();
            String sql="select d.id,d.ticket_id,t.user_id,a.username,d.book_id,b.title,d.quantity,"+
            "t.borrow_date,t.due_date,d.return_date,d.fine_amount,d.note,t.status"+
            " from borrow_details d join borrow_tickets t on d.ticket_id=t.id"+
            " join users u on u.id=t.user_id"+
            " join accounts a on a.user_id=u.id"+
            " join books b on b.id=d.book_id"+
            " order by d.id desc";
            try(Connection con=DatabaseConnection.getConnection();
                PreparedStatement ps=con.prepareStatement(sql);
                ResultSet rs=ps.executeQuery()){
                    while (rs.next()) {
                        BorrowViewDTO borrowViewDTO=new BorrowViewDTO();
                        borrowViewDTO.setId(rs.getInt("id"));
                        borrowViewDTO.setTicketId(rs.getInt("ticket_id"));
                        borrowViewDTO.setUserId(rs.getInt("user_id"));
                        borrowViewDTO.setUsername(rs.getString("username"));
                        borrowViewDTO.setBookId(rs.getInt("book_id"));
                        borrowViewDTO.setBookTitle(rs.getString("title"));
                        borrowViewDTO.setQuantity(rs.getInt("quantity"));
                        borrowViewDTO.setBorrowDate(rs.getObject("borrow_date",LocalDate.class));
                        borrowViewDTO.setDueDate(rs.getObject("due_date",LocalDate.class));
                        borrowViewDTO.setReturnDate(rs.getObject("return_date",LocalDate.class));
                        borrowViewDTO.setFineAmount(rs.getDouble("fine_amount"));                                                                                                                                                                                                    
                        borrowViewDTO.setNote(rs.getString("note"));
                        borrowViewDTO.setStatus(rs.getString("status"));
                    list.add(borrowViewDTO);
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
                return list;
        }
        //kiểm tra số lượng sách trong kho
        public int getAvailableQuantity(int BookId){
            String sql="SELECT available_quantity FROM books WHERE id=?";
            try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
                ps.setInt(1, BookId);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next())
                        return rs.getInt("available_quantity");
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return 0;
        }
        public boolean insertBorrow(int userId,int bookId,int quantity,LocalDate  borrowDate,LocalDate  dueDate) {
            String sqlTicket="INSERT INTO borrow_tickets (user_id, borrow_date,due_date,status) VALUES (?,?,?,'BORROWING')";
            String sqlDetail="INSERT INTO borrow_details (ticket_id,book_id,return_date,fine_amount,note,quantity) VALUES (?,?,NULL,0,NULL,?)";
            String sqlUpdateBook="UPDATE books SET available_quantity = available_quantity-? where id=?";
            try(Connection con=DatabaseConnection.getConnection()){
                con.setAutoCommit(false);
                int ticketId=-1;
                try(PreparedStatement psTicket=con.prepareStatement(sqlTicket,Statement.RETURN_GENERATED_KEYS)){
                    psTicket.setInt(1, userId);
                    psTicket.setObject(2, borrowDate);
                    psTicket.setObject(3, dueDate);
                    psTicket.executeUpdate();
                    try(ResultSet rs=psTicket.getGeneratedKeys()){
                        if(rs.next()) {
                            ticketId=rs.getInt(1);
                        }
                    }
                }
                if(ticketId==-1){
                    con.rollback();
                    return false;
                }
                try(PreparedStatement psDetail=con.prepareStatement(sqlDetail)){
                    psDetail.setInt(1, ticketId);
                    psDetail.setInt(2, bookId);
                    psDetail.setInt(3, quantity);
                    psDetail.executeUpdate();
                }
                try(PreparedStatement psUpdateBook=con.prepareStatement(sqlUpdateBook)){
                    psUpdateBook.setInt(1, quantity);
                    psUpdateBook.setInt(2, bookId);
                    psUpdateBook.executeUpdate();
                }
                con.commit();
                return true;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public boolean returnBook(int detailId){
            String sqlFind="SELECT d.ticket_id,d.book_id,t.due_date,d.quantity FROM borrow_details d"+
            " JOIN borrow_tickets t on d.ticket_id=t.id where d.id=?";
            String sqlUpdateDetail="UPDATE borrow_details SET return_date=?, fine_amount=?,note=? where id=?";
            String sqlUpdateTicket="UPDATE borrow_tickets SET status=? WHERE id=?";
            //kiêm tra sách chưa trả
            String sqlCheckRemaining="SELECT COUNT(*) FROM borrow_details WHERE ticket_id=? AND  return_date IS NULL";
            String sqlReturnBook="UPDATE books SET available_quantity=available_quantity+? WHERE id=?";
            try(Connection con=DatabaseConnection.getConnection()){
                con.setAutoCommit(false);
                int ticketId=-1,bookId=-1,quantity=1;
                LocalDate dueDate=null;
                try(PreparedStatement psFind=con.prepareStatement(sqlFind)){
                    psFind.setInt(1, detailId);
                    try(ResultSet rs=psFind.executeQuery()){
                        if(rs.next()){
                            ticketId=rs.getInt("ticket_id");
                            bookId=rs.getInt("book_id");
                            quantity=rs.getInt("quantity");
                            dueDate=rs.getObject("due_date", LocalDate.class);
                        }
                    }
                }
                if(ticketId==-1||dueDate==null){
                    con.rollback();
                    return false;
                }
                LocalDate today=LocalDate.now();
                long hanTra=ChronoUnit.DAYS.between(dueDate, today);
                
                double fineAmount=0;
                String note="bình thương";
                if(hanTra>0){
                    fineAmount=hanTra*5000;
                    note="trả muộn"+hanTra+" ngày";
                }
                try(PreparedStatement psUpdateDetail=con.prepareStatement(sqlUpdateDetail)){
                    psUpdateDetail.setObject(1, today);
                    psUpdateDetail.setDouble(2, fineAmount);
                    psUpdateDetail.setString(3, note);
                    psUpdateDetail.setInt(4, detailId);
                    psUpdateDetail.executeUpdate();
                }
                boolean isAllReturn=false;
                try(PreparedStatement psCheck=con.prepareStatement(sqlCheckRemaining)){
                    psCheck.setInt(1, ticketId);
                    try(ResultSet rs = psCheck.executeQuery()){
                        if(rs.next() && rs.getInt(1)==0){
                         isAllReturn = true;
                        }
                    }
                 }
               if (isAllReturn) {
                try(PreparedStatement psUpdateTicket=con.prepareStatement(sqlUpdateTicket)){
                    psUpdateTicket.setString(1, "RETURNED");
                    psUpdateTicket.setInt(2, ticketId);
                    psUpdateTicket.executeUpdate();
                }
               }
                try(PreparedStatement psReturnBook=con.prepareStatement(sqlReturnBook)){
                    psReturnBook.setInt(1, quantity);
                    psReturnBook.setInt(2, bookId);
                    psReturnBook.executeUpdate();
                }
                con.commit();
                return true;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public boolean userExists(int userId){
            String sql="SELECT id FROM users WHERE id=?";
            try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
                ps.setInt(1, userId);
                try(ResultSet rs=ps.executeQuery()){
                    return rs.next();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public boolean updateBorrow(int detailId,int userId,int bookId,int quantity,LocalDate dueDate,String status){
            String sqlTicket="UPDATE borrow_tickets t "+
            "JOIN borrow_details d on t.id=d.ticket_id "+
            "SET t.user_id=? ,t.due_date=?, t.status=? "+
            "WHERE d.id=?";
            String sqlDetail="UPDATE borrow_details SET book_id=?,quantity=? WHERE id=?";
            try(Connection con=DatabaseConnection.getConnection()){
                con.setAutoCommit(false);
                try(PreparedStatement psTicket=con.prepareStatement(sqlTicket)){
                    psTicket.setInt(1, userId);
                    psTicket.setObject(2, dueDate);
                    psTicket.setString(3, status);
                    psTicket.setInt(4, detailId);
                    psTicket.executeUpdate();
                }
                try(PreparedStatement psDetail=con.prepareStatement(sqlDetail)){
                    psDetail.setInt(1, bookId);
                    psDetail.setInt(2, quantity);
                    psDetail.setInt(3, detailId);
                    psDetail.executeUpdate();
                }
                if("BORROWING".equals(status)){
                    String sqlReset="UPDATE borrow_details "+
                    "SET return_date=NULL, fine_amount=0, note=NULL "+
                    "WHERE id=?";
                    try(PreparedStatement ps=con.prepareStatement(sqlReset)){
                        ps.setInt(1, detailId);
                        ps.executeUpdate();
                    }
                }
                else if("RETURNED".equals(status)){
                    String sqlSetReturnDate="UPDATE borrow_details"+
                    " SET return_date=COALESCE(return_date, ?)"+
                    " WHERE id=?";
                    try(PreparedStatement psReturn=con.prepareStatement(sqlSetReturnDate)){
                        psReturn.setObject(1, LocalDate.now());
                        psReturn.setInt(2, detailId);
                        psReturn.executeUpdate();

                    }
                }
                con.commit();
                return true;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public List<BorrowViewDTO> getBorrow(String keyword){
            List<BorrowViewDTO> list=new ArrayList<>();
            String sql="select d.id,d.ticket_id,t.user_id,a.username,d.book_id,b.title,d.quantity,"+
            "t.borrow_date,t.due_date,d.return_date,d.fine_amount,d.note,t.status"+
            " from borrow_details d join borrow_tickets t on d.ticket_id=t.id"+
            " join users u on u.id=t.user_id"+
            " join accounts a on a.user_id=u.id"+
            " join books b on b.id=d.book_id"+
            " WHERE CAST(d.ticket_id AS CHAR) LIKE ? "+
            "OR CAST(t.user_id AS CHAR) LIKE ? "+
            "OR username LIKE ? "+
            "or b.title LIKE ? " +
             "or t.status LIKE ? " +
            " order by d.id desc";
            try(Connection con=DatabaseConnection.getConnection();
               PreparedStatement psBorrow=con.prepareStatement(sql)){
                String query="%" + keyword +"%";
                psBorrow.setString(1, query);
                psBorrow.setString(2, query);
                psBorrow.setString(3, query);
                psBorrow.setString(4, query);
                psBorrow.setString(5, query);
               ResultSet rs = psBorrow.executeQuery();
               while (rs.next()) {
                BorrowViewDTO borrowViewDTO=new BorrowViewDTO();
                borrowViewDTO.setId(rs.getInt("id"));
                borrowViewDTO.setTicketId(rs.getInt("ticket_id"));
                borrowViewDTO.setUserId(rs.getInt("user_id"));
                borrowViewDTO.setUsername(rs.getString("username"));
                borrowViewDTO.setBookId(rs.getInt("book_id"));
                borrowViewDTO.setBookTitle(rs.getString("title"));
                borrowViewDTO.setBorrowDate(rs.getObject("borrow_date",LocalDate.class));
                borrowViewDTO.setDueDate(rs.getObject("due_date",LocalDate.class));
                borrowViewDTO.setReturnDate(rs.getObject("return_date",LocalDate.class));
                borrowViewDTO.setFineAmount(rs.getDouble("fine_amount"));
                borrowViewDTO.setNote(rs.getString("note"));
                borrowViewDTO.setStatus(rs.getString("status"));
                borrowViewDTO.setQuantity(rs.getInt("quantity"));
                list.add(borrowViewDTO);
               }
               }catch(SQLException e){
                e.printStackTrace();
               }
               return list;
        }
    }
