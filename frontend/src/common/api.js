import axios from "axios";
import URL from "../service/URL";

export const registerUser = async (username, password) => {
  try {
    const response = await axios.post(
      `${URL}/api/auth/register`,
      {
        username,
        password,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const login = async (username, password) => {
  try {
    const response = await axios.post(
      `${URL}/api/auth/authenticate`,
      {
        username,
        password,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const whoAmI = async (token) => {
  try {
    const response = await axios.get(`${URL}/api/users/me`, {
      headers: {
        authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const getUsers = async () => {
  try {
    const response = await axios.get(`${URL}/api/users/all`, {
      headers: {
        authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const sendTransaction = async (recipient, value) => {
  try {
    const user = JSON.parse(localStorage.getItem("user"));
    const response = await axios.post(
      `${URL}/api/transaction`,
      {
        sender: user.id,
        recipient,
        value,
      },
      {
        headers: {
          authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const getBlockchain = async () => {
  try {
    const response = await axios.get(`${URL}/api/blockchain`, {
      headers: {
        authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};

export const getPendingTransactions = async () => {
  try {
    const response = await axios.get(`${URL}/api/transaction`, {
      headers: {
        authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (e) {
    throw new Error(e?.response?.data);
  }
};
