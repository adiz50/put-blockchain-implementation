import { useEffect, useState } from "react";
import {
  getBlockchain,
  getPendingTransactions,
  getUsers,
  sendTransaction,
} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";
import {
  Layout,
  Modal,
  Button,
  Space,
  Input,
  Typography,
  InputNumber,
  Select,
  theme,
  Divider,
} from "antd";
import BlockchainGraph from "./Graph";
import PendingTransactionsColumn from "./Pending";

const Dashboard = ({ snackbar }) => {
  const [allUsers, setAllUsers] = useState([]);
  const [blockchain, setBlockchain] = useState([]);
  const [pendingTransactions, setPendingTransactions] = useState([]);
  const { token } = theme.useToken();
  const [newTransaction, setNewTransaction] = useState({
    recipient: "",
    amount: 0,
    open: false,
  });
  const currentUser = JSON.parse(localStorage.getItem("user"));

  const getAllUsers = () => {
    getUsers()
      .then((data) => {
        setAllUsers(data);
      })
      .catch((error) => {
        snackbar(error.message, "error");
      });
  };

  const getTransactions = () => {
    getBlockchain()
      .then((data) => {
        setBlockchain(data?.reverse());
      })
      .catch((error) => {
        snackbar(error.message, "error");
      });
  };

  const getWatiningTransactions = () => {
    getPendingTransactions()
      .then((data) => {
        setPendingTransactions(data);
      })
      .catch((error) => {
        snackbar(error.message, "error");
      });
  };

  const handleTransaction = (id, amount) => {
    sendTransaction(id, amount)
      .then((data) => {
        snackbar("Transaction sent", "success");
        getTransactions();
        getWatiningTransactions();
        handleClose();
      })
      .catch((error) => {
        snackbar(error.message, "error");
      });
  };

  const handleClose = () => {
    setNewTransaction({
      recipient: "",
      amount: 0,
      open: false,
    });
  };

  useEffect(() => {
    const getAllData = () => {
      getAllUsers();
      getTransactions();
      getWatiningTransactions();
    };

    const intervalId = setInterval(getAllData, 5000);

    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(getAllUsers, []);
  useEffect(getTransactions, []);
  useEffect(getWatiningTransactions, []);

  return (
    <>
      <Layout.Header
        style={{
          background: token.colorBgContainer,
        }}
      >
        <Space
          direction="horizontal"
          align="center"
          style={{
            height: "100%",
            width: "100%",
            justifyContent: "space-between",
          }}
        >
          <Typography.Title level={3}>Dashboard</Typography.Title>
          <Space size={12}>
            <Button
              type="primary"
              onClick={() => {
                setNewTransaction({
                  ...newTransaction,
                  open: true,
                });
              }}
            >
              New transaction
            </Button>
            <Button
              type="link"
              onClick={() => {
                AuthenticationService.logout();
              }}
            >
              Logout
            </Button>
          </Space>
        </Space>
      </Layout.Header>
      <Layout.Content>
        <div
          style={{
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
          }}
        >
          <BlockchainGraph blockchain={blockchain} />
          <Divider type="vertical" style={{ height: "80%" }} />
          <PendingTransactionsColumn
            pendingTransactions={pendingTransactions}
          />
        </div>
      </Layout.Content>
      <Modal
        open={newTransaction.open}
        title="New transaction"
        centered
        onCancel={handleClose}
        footer={[
          <Button onClick={handleClose}>Cancel</Button>,
          <Button
            type="primary"
            onClick={() =>
              handleTransaction(newTransaction.recipient, newTransaction.amount)
            }
            disabled={
              !newTransaction.recipient ||
              parseInt(newTransaction.amount) === 0 ||
              parseInt(newTransaction.amount) <= 0
            }
          >
            Send
          </Button>,
        ]}
      >
        <Space size={12} direction="vertical" style={{ width: "100%" }}>
          <Space size={2} direction="vertical" style={{ width: "100%" }}>
            <Typography.Text>Recipient</Typography.Text>
            <Select
              value={newTransaction.recipient}
              style={{ width: "100%" }}
              placeholder="Select a person"
              allowClear
              onChange={(value) =>
                setNewTransaction({
                  ...newTransaction,
                  recipient: value,
                })
              }
            >
              {allUsers
                .filter((user) => user.id !== currentUser.id)
                .map((user) => (
                  <Select.Option value={user.id}>{user.username}</Select.Option>
                ))}
            </Select>
            <Input
              readOnly
              style={{ width: "100%" }}
              placeholder="ID"
              value={newTransaction.recipient}
            />
          </Space>
          <Space size={2} direction="vertical" style={{ width: "100%" }}>
            <Typography.Text>Amount</Typography.Text>
            <InputNumber
              style={{ width: "100%" }}
              placeholder="Amount"
              value={parseInt(newTransaction.amount)}
              onChange={(value) =>
                setNewTransaction({
                  ...newTransaction,
                  amount: parseInt(value),
                })
              }
              status={parseInt(newTransaction.amount) < 0 ? "error" : ""}
            />
          </Space>
        </Space>
      </Modal>
    </>
  );
};

export default Dashboard;
